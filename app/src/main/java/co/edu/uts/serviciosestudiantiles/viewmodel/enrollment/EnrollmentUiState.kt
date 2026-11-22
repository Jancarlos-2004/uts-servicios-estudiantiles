package co.edu.uts.serviciosestudiantiles.viewmodel.enrollment

import co.edu.uts.serviciosestudiantiles.data.model.ExtracurricularCourse
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalBenefit
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile

/**
 * Estado inmutable de la interfaz de usuario para el Módulo 2 (Liquidación Institucional y Derechos Pecuniarios).
 * Maneja vigencias oficiales (2026 y 2027), beneficios reglamentarios y acreditación de financiación ICETEX.
 */
data class EnrollmentUiState(
    val selectedVigencia: String = InstitutionalTariff.VIGENCIA_2027,
    val selectedTariff: InstitutionalTariff? = null,
    val availableTariffs: List<InstitutionalTariff> = emptyList(),
    val selectedBenefit: InstitutionalBenefit = InstitutionalBenefit.NINGUNO,
    val availableBenefits: List<InstitutionalBenefit> = InstitutionalBenefit.OFFICIAL_BENEFITS,
    val isIcetexFunding: Boolean = false,
    val selectedCourse: ExtracurricularCourse = ExtracurricularCourse.PROGRAMACION_WEB,
    val hasScholarship: Boolean = false,
    val hasIcetex: Boolean = false,
    val baseCost: Double = 2_836_466.0,
    val discountAmount: Double = 0.0,
    val subtotalAmount: Double = 2_836_466.0,
    val surchargeAmount: Double = 0.0,
    val totalAmount: Double = 2_836_466.0,
    val isProfileCompleted: Boolean = false,
    val currentStudent: StudentProfile? = null,
    val isProcessing: Boolean = false,
    val successMessage: String? = null,
    val securityLockMessage: String? = null,
    val tariffSource: String = "Resolución UTS No. 02-1613 del 15 de septiembre de 2026"
) {
    /**
     * El botón de liquidación solo puede habilitarse si existe un perfil académico válido
     * (Bloqueo de Seguridad obligatorio).
     */
    val canLiquidate: Boolean
        get() = isProfileCompleted && !isProcessing
}
