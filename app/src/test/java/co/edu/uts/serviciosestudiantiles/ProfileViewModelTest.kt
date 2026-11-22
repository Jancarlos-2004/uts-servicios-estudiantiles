package co.edu.uts.serviciosestudiantiles

import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.testutil.FakeProfileRepository
import co.edu.uts.serviciosestudiantiles.viewmodel.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeProfileRepository
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProfileRepository()
        viewModel = ProfileViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun calculateInstitutionalEmail_normalizesAccentsAndSpacesCorrectly() {
        // Caso: Nombre con tildes y espacios múltiples + código
        val email1 = viewModel.calculateInstitutionalEmail("Carlos Andrés Pérez", "10987654")
        assertEquals("carlos.andres.perez.10987654@uts.edu.co", email1)

        // Caso: Nombre con ñ y mayúsculas
        val email2 = viewModel.calculateInstitutionalEmail("María Nuñez", "2024100")
        assertEquals("maria.nunez.2024100@uts.edu.co", email2)

        // Caso: Espacios en blanco excesivos
        val email3 = viewModel.calculateInstitutionalEmail("  Juan   Pablo  ", "998877")
        assertEquals("juan.pablo.998877@uts.edu.co", email3)
    }

    @Test
    fun onFullNameChanged_updatesStateAndRecalculatesEmail() {
        viewModel.onStudentCodeChanged("10987")
        viewModel.onFullNameChanged("Carlos Perez")

        val state = viewModel.uiState.value
        assertEquals("Carlos Perez", state.fullName)
        assertEquals("carlos.perez.10987@uts.edu.co", state.generatedEmail)
        assertNull(state.nameError)
    }

    @Test
    fun onFullNameChanged_withLessThan3Characters_setsValidationError() {
        viewModel.onFullNameChanged("Al")
        val state = viewModel.uiState.value
        assertNotNull(state.nameError)
    }

    @Test
    fun onSaveProfile_withEmptyFields_failsAndShowsErrors() = runTest {
        viewModel.onSaveProfile()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSaved)
        assertNotNull(state.nameError)
        assertNotNull(state.codeError)
        assertNotNull(state.facultyError)
    }

    @Test
    fun onSaveProfile_withValidFields_savesSuccessfully() = runTest {
        viewModel.onFullNameChanged("Estudiante UTS")
        viewModel.onStudentCodeChanged("123456")
        viewModel.onFacultySelected(Faculty.INGENIERIAS)

        viewModel.onSaveProfile()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSaved)
        assertNotNull(state.successMessage)
        assertNotNull(state.currentProfile)
        assertEquals("estudiante.uts.123456@uts.edu.co", state.currentProfile?.institutionalEmail)

        // Verificar persistencia en el repositorio
        val savedInRepo = fakeRepository.getProfile()
        assertNotNull(savedInRepo)
        assertEquals("Estudiante UTS", savedInRepo?.fullName)
        assertEquals(Faculty.INGENIERIAS, savedInRepo?.faculty)
    }

    @Test
    fun loadExistingProfile_loadsProfileIntoStateOnInit() = runTest {
        // Pre-cargar perfil en repositorio
        fakeRepository.saveProfile(
            StudentProfile(
                fullName = "Laura Gómez",
                studentCode = "987654",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS,
                institutionalEmail = "laura.gomez.987654@uts.edu.co"
            )
        )

        // Crear nuevo ViewModel que cargará el perfil existente
        val newViewModel = ProfileViewModel(fakeRepository)
        advanceUntilIdle()

        val state = newViewModel.uiState.value
        assertTrue(state.isSaved)
        assertEquals("Laura Gómez", state.fullName)
        assertEquals("987654", state.studentCode)
        assertEquals(Faculty.CIENCIAS_SOCIOECONOMICAS, state.selectedFaculty)
        assertEquals("laura.gomez.987654@uts.edu.co", state.generatedEmail)
    }
}

