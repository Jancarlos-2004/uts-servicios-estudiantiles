package co.edu.uts.serviciosestudiantiles.viewmodel.enrollment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import co.edu.uts.serviciosestudiantiles.data.model.ExtracurricularCourse
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalBenefit
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.data.model.TuitionLiquidation
import co.edu.uts.serviciosestudiantiles.data.repository.DefaultInstitutionalRepository
import co.edu.uts.serviciosestudiantiles.data.repository.InstitutionalRepository
import co.edu.uts.serviciosestudiantiles.data.repository.ProfileRepository
import co.edu.uts.serviciosestudiantiles.data.repository.TuitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.round

/**
 * ViewModel del Módulo 2: Liquidación de Derechos Pecuniarios y Matrícula UTS.
 *
 * Implementa:
 * - Tarifas oficiales por vigencia (2026 y 2027 según Resolución UTS No. 02-1613).
 * - Aplicación oficial de estímulos y beneficios (Beca de Excelencia Acuerdo 01-035, Gratuidad, Descuento Electoral).
 * - Financiación con ICETEX sin recargos ficticios (0% de recargo institucional).
 * - Bloqueo de Seguridad: impide liquidar si no existe perfil académico registrado en el Módulo 1.
 * - Encapsulación UDF (MutableStateFlow privado -> StateFlow público).
 * - Persistencia del comprobante oficial en TuitionRepository.
 */
class EnrollmentViewModel(
    private val profileRepository: ProfileRepository,
    private val tuitionRepository: TuitionRepository,
    private val institutionalRepository: InstitutionalRepository = DefaultInstitutionalRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnrollmentUiState())
    val uiState: StateFlow<EnrollmentUiState> = _uiState.asStateFlow()

    init {
        observeStudentProfile()
        loadInitialTariffs()
    }

    private fun loadInitialTariffs() {
        val vigencia = InstitutionalTariff.VIGENCIA_2027
        val tariffs = institutionalRepository.getTariffs(vigencia)
        val defaultTariff = tariffs.firstOrNull { it.id == "TAR-2027-MAT-TEC" } ?: tariffs.firstOrNull()

        _uiState.update { current ->
            current.copy(
                selectedVigencia = vigencia,
                availableTariffs = tariffs,
                selectedTariff = defaultTariff,
                baseCost = defaultTariff?.amount ?: 2_836_466.0,
                subtotalAmount = defaultTariff?.amount ?: 2_836_466.0,
                totalAmount = defaultTariff?.amount ?: 2_836_466.0,
                tariffSource = defaultTariff?.officialAct ?: "Resolución UTS No. 02-1613 del 15 de septiembre de 2026"
            )
        }
    }

    private fun observeStudentProfile() {
        viewModelScope.launch {
            profileRepository.getProfileFlow().collect { profile ->
                val isCompleted = profile != null && profile.isValid
                _uiState.update { current ->
                    current.copy(
                        isProfileCompleted = isCompleted,
                        currentStudent = profile,
                        securityLockMessage = if (!isCompleted) {
                            "Para liquidar derechos pecuniarios o matrículas primero debes completar tu registro en el Módulo de Perfil Académico."
                        } else null
                    )
                }
            }
        }
    }

    /**
     * Evento: Cambio de vigencia oficial ("2026" o "2027").
     */
    fun onVigenciaSelected(vigencia: String) {
        val tariffs = institutionalRepository.getTariffs(vigencia)
        val defaultTariff = tariffs.firstOrNull()
        recalculateWithTariff(
            tariff = defaultTariff,
            vigencia = vigencia,
            benefit = _uiState.value.selectedBenefit,
            isIcetex = _uiState.value.isIcetexFunding
        )
    }

    /**
     * Evento: Selección de concepto/tarifa oficial.
     */
    fun onTariffSelected(tariff: InstitutionalTariff) {
        recalculateWithTariff(
            tariff = tariff,
            vigencia = _uiState.value.selectedVigencia,
            benefit = _uiState.value.selectedBenefit,
            isIcetex = _uiState.value.isIcetexFunding
        )
    }

    /**
     * Evento: Selección de beneficio o estímulo institucional verificado.
     */
    fun onBenefitSelected(benefit: InstitutionalBenefit) {
        recalculateWithTariff(
            tariff = _uiState.value.selectedTariff,
            vigencia = _uiState.value.selectedVigencia,
            benefit = benefit,
            isIcetex = _uiState.value.isIcetexFunding
        )
    }

    /**
     * Evento: Alternancia de la modalidad de financiación ICETEX.
     * En la UTS NO existe recargo del 5%; ICETEX financia el valor neto de matrícula.
     */
    fun onIcetexFundingToggled(enabled: Boolean) {
        recalculateWithTariff(
            tariff = _uiState.value.selectedTariff,
            vigencia = _uiState.value.selectedVigencia,
            benefit = _uiState.value.selectedBenefit,
            isIcetex = enabled
        )
    }

    private fun recalculateWithTariff(
        tariff: InstitutionalTariff?,
        vigencia: String,
        benefit: InstitutionalBenefit,
        isIcetex: Boolean
    ) {
        val tariffs = institutionalRepository.getTariffs(vigencia)
        val selectedTariff = tariff ?: tariffs.firstOrNull()
        val base = selectedTariff?.amount ?: 0.0

        // Descuento según porcentaje oficial del beneficio
        val discount = round(base * benefit.discountPercentage)
        val subtotal = (base - discount).coerceAtLeast(0.0)

        // OFICIAL: ICETEX es una modalidad de crédito, la UTS no impone recargo del 5%
        val surcharge = 0.0
        val total = subtotal + surcharge

        val hasScholarship = benefit.discountPercentage > 0.0

        _uiState.update { current ->
            current.copy(
                selectedVigencia = vigencia,
                availableTariffs = tariffs,
                selectedTariff = selectedTariff,
                selectedBenefit = benefit,
                isIcetexFunding = isIcetex,
                hasScholarship = hasScholarship,
                hasIcetex = isIcetex,
                baseCost = base,
                discountAmount = discount,
                subtotalAmount = subtotal,
                surchargeAmount = surcharge,
                totalAmount = total,
                tariffSource = selectedTariff?.officialAct ?: "Resolución Oficial UTS",
                successMessage = null
            )
        }
    }

    // =========================================================================
    // MÉTODOS DE COMPATIBILIDAD HACIA ATRÁS
    // =========================================================================

    fun onCourseSelected(course: ExtracurricularCourse) {
        val base = course.basePrice
        val discount = if (_uiState.value.hasScholarship) round(base * 0.50) else 0.0
        val subtotal = base - discount
        val total = subtotal

        _uiState.update { current ->
            current.copy(
                selectedCourse = course,
                baseCost = base,
                discountAmount = discount,
                subtotalAmount = subtotal,
                totalAmount = total,
                successMessage = null
            )
        }
    }

    fun onScholarshipToggled(enabled: Boolean) {
        val benefit = if (enabled) InstitutionalBenefit.BECA_EXCELENCIA_50 else InstitutionalBenefit.NINGUNO
        onBenefitSelected(benefit)
    }

    fun onIcetexToggled(enabled: Boolean) {
        onIcetexFundingToggled(enabled)
    }

    /**
     * Acción de Liquidación con verificación estricta del Bloqueo de Seguridad.
     */
    fun onLiquidate() {
        val state = _uiState.value

        // Verificación estricta del Bloqueo de Seguridad
        if (!state.isProfileCompleted || state.currentStudent == null) {
            _uiState.update {
                it.copy(
                    securityLockMessage = "Operación denegada: debes completar el perfil académico antes de liquidar."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            val conceptName = state.selectedTariff?.conceptName ?: state.selectedCourse.title
            val tariffSource = state.selectedTariff?.officialAct ?: "Resolución UTS No. 02-1613"

            val liquidation = TuitionLiquidation(
                id = 0L,
                courseName = conceptName,
                baseCost = state.baseCost,
                hasScholarship = state.hasScholarship,
                hasIcetex = state.hasIcetex,
                discountAmount = state.discountAmount,
                surchargeAmount = state.surchargeAmount,
                totalAmount = state.totalAmount,
                studentCode = state.currentStudent.studentCode,
                studentName = state.currentStudent.fullName,
                vigencia = state.selectedVigencia,
                benefitDescription = state.selectedBenefit.name,
                tariffSource = tariffSource,
                timestamp = System.currentTimeMillis()
            )

            tuitionRepository.saveLiquidation(liquidation)

            _uiState.update {
                it.copy(
                    isProcessing = false,
                    successMessage = "¡Liquidación oficial de $conceptName (Vigencia ${state.selectedVigencia}) registrada con éxito!"
                )
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    companion object {
        fun provideFactory(
            profileRepository: ProfileRepository,
            tuitionRepository: TuitionRepository,
            institutionalRepository: InstitutionalRepository = DefaultInstitutionalRepository()
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EnrollmentViewModel(profileRepository, tuitionRepository, institutionalRepository) as T
                }
            }
    }
}
