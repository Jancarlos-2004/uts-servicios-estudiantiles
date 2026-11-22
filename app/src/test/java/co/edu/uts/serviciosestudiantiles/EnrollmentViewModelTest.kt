package co.edu.uts.serviciosestudiantiles

import co.edu.uts.serviciosestudiantiles.data.model.AcademicProgram
import co.edu.uts.serviciosestudiantiles.data.model.ExtracurricularCourse
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalBenefit
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.testutil.FakeProfileRepository
import co.edu.uts.serviciosestudiantiles.testutil.FakeTuitionRepository
import co.edu.uts.serviciosestudiantiles.viewmodel.enrollment.EnrollmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EnrollmentViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var profileRepository: FakeProfileRepository
    private lateinit var tuitionRepository: FakeTuitionRepository
    private lateinit var viewModel: EnrollmentViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        profileRepository = FakeProfileRepository()
        tuitionRepository = FakeTuitionRepository()
        viewModel = EnrollmentViewModel(profileRepository, tuitionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun officialTariffs2027_initialSelection_matchesResolution021613() {
        val state = viewModel.uiState.value
        assertEquals(InstitutionalTariff.VIGENCIA_2027, state.selectedVigencia)
        // Matrícula Tecnológica 2027 oficial: $2.836.466 COP
        assertEquals(2_836_466.0, state.baseCost, 0.01)
        assertEquals(2_836_466.0, state.totalAmount, 0.01)
        assertEquals(0.0, state.surchargeAmount, 0.01)
    }

    @Test
    fun vigenciaSwitch_changesTariffValuesBetween2026and2027() {
        // Cambiar a Vigencia 2026
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2026)
        val state2026 = viewModel.uiState.value
        assertEquals(InstitutionalTariff.VIGENCIA_2026, state2026.selectedVigencia)
        // Matrícula Tecnológica 2026: $2.626.358 COP
        assertEquals(2_626_358.0, state2026.baseCost, 0.01)

        // Cambiar a Vigencia 2027
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        val state2027 = viewModel.uiState.value
        assertEquals(InstitutionalTariff.VIGENCIA_2027, state2027.selectedVigencia)
        assertEquals(2_836_466.0, state2027.baseCost, 0.01)
    }

    @Test
    fun universityLevelTariff_matchesResolution021613() {
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        val universityTariff = viewModel.uiState.value.availableTariffs.first { it.id == "TAR-2027-MAT-UNI" }
        viewModel.onTariffSelected(universityTariff)

        val state = viewModel.uiState.value
        // Matrícula Universitaria 2027: $3.781.955 COP
        assertEquals(3_781_955.0, state.baseCost, 0.01)
        assertEquals(3_781_955.0, state.totalAmount, 0.01)
    }

    @Test
    fun englishCourseTariff_forStudentsMatchesResolution021613() {
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        val englishTariff = viewModel.uiState.value.availableTariffs.first { it.id == "TAR-2027-ING-EST" }
        viewModel.onTariffSelected(englishTariff)

        val state = viewModel.uiState.value
        // Curso de inglés estudiantes UTS: $416.015 COP
        assertEquals(416_015.0, state.baseCost, 0.01)
        assertEquals(416_015.0, state.totalAmount, 0.01)
    }

    @Test
    fun excellenceScholarship_applies50PercentDiscountAccordingToAcuerdo01035() {
        // Matrícula Tecnológica 2027: $2.836.466
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        viewModel.onBenefitSelected(InstitutionalBenefit.BECA_EXCELENCIA_50)

        val state = viewModel.uiState.value
        assertEquals(2_836_466.0, state.baseCost, 0.01)
        // 50% de descuento = $1.418.233 COP
        assertEquals(1_418_233.0, state.discountAmount, 0.01)
        assertEquals(1_418_233.0, state.subtotalAmount, 0.01)
        assertEquals(1_418_233.0, state.totalAmount, 0.01)
        assertEquals(0.0, state.surchargeAmount, 0.01)
    }

    @Test
    fun electoralDiscount_applies10PercentDiscountAccordingToLey403() {
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        viewModel.onBenefitSelected(InstitutionalBenefit.DESCUENTO_ELECTORAL_10)

        val state = viewModel.uiState.value
        assertEquals(2_836_466.0, state.baseCost, 0.01)
        // 10% de descuento = $283.647 COP
        assertEquals(283_647.0, state.discountAmount, 0.01)
        assertEquals(2_552_819.0, state.totalAmount, 0.01)
    }

    @Test
    fun gratuidadPolicy_covers100PercentOfMatricula() {
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        viewModel.onBenefitSelected(InstitutionalBenefit.POLITICA_GRATUIDAD_100)

        val state = viewModel.uiState.value
        assertEquals(2_836_466.0, state.baseCost, 0.01)
        assertEquals(2_836_466.0, state.discountAmount, 0.01)
        assertEquals(0.0, state.totalAmount, 0.01)
    }

    @Test
    fun icetexFunding_hasZeroSurchargeInUtsOfficialRules() {
        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        viewModel.onIcetexFundingToggled(true)

        val state = viewModel.uiState.value
        // La UTS no cobra recargo del 5% por financiamiento ICETEX
        assertEquals(0.0, state.surchargeAmount, 0.01)
        assertEquals(state.baseCost, state.totalAmount, 0.01)
    }

    @Test
    fun securityLock_blocksLiquidationWhenProfileIsEmpty() = runTest {
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse("El botón liquidar debe estar deshabilitado", state.canLiquidate)
        assertFalse(state.isProfileCompleted)
        assertNotNull(state.securityLockMessage)

        viewModel.onLiquidate()
        advanceUntilIdle()

        val liquidations = tuitionRepository.getAllLiquidationsFlow().first()
        assertTrue("No debe guardarse ninguna liquidación si no hay perfil", liquidations.isEmpty())
    }

    @Test
    fun securityLock_unblocksWhenProfileIsRegistered_andLiquidatesOfficialTariffSuccessfully() = runTest {
        profileRepository.saveProfile(
            StudentProfile(
                fullName = "Andrés Gómez",
                studentCode = "1098700",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                institutionalEmail = "andres.gomez.1098700@uts.edu.co",
                programName = "Tecnología en Desarrollo de Sistemas Informáticos"
            )
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("El perfil debe marcarse como completado", state.isProfileCompleted)
        assertTrue("El botón liquidar debe estar habilitado", state.canLiquidate)
        assertNull(state.securityLockMessage)

        viewModel.onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027)
        viewModel.onBenefitSelected(InstitutionalBenefit.BECA_EXCELENCIA_50)

        viewModel.onLiquidate()
        advanceUntilIdle()

        val updatedState = viewModel.uiState.value
        assertNotNull(updatedState.successMessage)

        val savedList = tuitionRepository.getAllLiquidationsFlow().first()
        assertEquals(1, savedList.size)
        val record = savedList[0]
        assertEquals("Matrícula Programas Tecnológicos", record.courseName)
        assertEquals(2_836_466.0, record.baseCost, 0.01)
        assertEquals(1_418_233.0, record.discountAmount, 0.01)
        assertEquals(1_418_233.0, record.totalAmount, 0.01)
        assertEquals("2027", record.vigencia)
        assertEquals("1098700", record.studentCode)
        assertEquals("Andrés Gómez", record.studentName)
    }
}
