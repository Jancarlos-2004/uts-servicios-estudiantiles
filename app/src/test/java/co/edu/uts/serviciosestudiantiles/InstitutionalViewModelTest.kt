package co.edu.uts.serviciosestudiantiles

import co.edu.uts.serviciosestudiantiles.data.model.AcademicLevel
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.UserCategory
import co.edu.uts.serviciosestudiantiles.data.repository.DefaultInstitutionalRepository
import co.edu.uts.serviciosestudiantiles.viewmodel.institutional.InstitutionalViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InstitutionalViewModelTest {

    private lateinit var repository: DefaultInstitutionalRepository
    private lateinit var viewModel: InstitutionalViewModel

    @Before
    fun setUp() {
        repository = DefaultInstitutionalRepository()
        viewModel = InstitutionalViewModel(repository)
    }

    @Test
    fun loadInitialData_containsBothOfficialFaculties() {
        val state = viewModel.uiState.value
        assertFalse(state.filteredPrograms.isEmpty())

        val ingenieriaPrograms = state.filteredPrograms.filter { it.faculty == Faculty.CIENCIAS_NATURALES_E_INGENIERIAS }
        val socioeconomicaPrograms = state.filteredPrograms.filter { it.faculty == Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES }

        assertTrue("Debe contener programas de la Facultad de Ciencias Naturales e Ingenierías", ingenieriaPrograms.isNotEmpty())
        assertTrue("Debe contener programas de la Facultad de Ciencias Socioeconómicas y Empresariales", socioeconomicaPrograms.isNotEmpty())
    }

    @Test
    fun filterByFaculty_returnsOnlyProgramsOfThatFaculty() {
        viewModel.onFacultyFilterSelected(Faculty.CIENCIAS_NATURALES_E_INGENIERIAS)
        val state = viewModel.uiState.value
        assertTrue(state.filteredPrograms.all { it.faculty == Faculty.CIENCIAS_NATURALES_E_INGENIERIAS })

        viewModel.onFacultyFilterSelected(Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES)
        val stateSocio = viewModel.uiState.value
        assertTrue(stateSocio.filteredPrograms.all { it.faculty == Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES })
    }

    @Test
    fun searchBySniesOrName_findsExactProgram() {
        // Buscar por código SNIES oficial de Tecnología en Desarrollo de Sistemas Informáticos (53488)
        viewModel.onSearchQueryChanged("53488")
        val state = viewModel.uiState.value
        assertEquals(1, state.filteredPrograms.size)
        assertEquals("Tecnología en Desarrollo de Sistemas Informáticos", state.filteredPrograms.first().name)

        // Buscar por nombre
        viewModel.onSearchQueryChanged("Contaduría")
        val stateCont = viewModel.uiState.value
        assertTrue(stateCont.filteredPrograms.any { it.name.contains("Contaduría", ignoreCase = true) })
    }

    @Test
    fun tariffs2027_containResolution021613Tariffs() {
        val state = viewModel.uiState.value
        val tariffs2027 = state.tariffs2027
        assertFalse(tariffs2027.isEmpty())

        val matriculaTec = tariffs2027.firstOrNull { it.id == "TAR-2027-MAT-TEC" }
        assertNotNull(matriculaTec)
        assertEquals(2_836_466.0, matriculaTec?.amount ?: 0.0, 0.01)

        val matriculaUni = tariffs2027.firstOrNull { it.id == "TAR-2027-MAT-UNI" }
        assertNotNull(matriculaUni)
        assertEquals(3_781_955.0, matriculaUni?.amount ?: 0.0, 0.01)

        val inglesEst = tariffs2027.firstOrNull { it.id == "TAR-2027-ING-EST" }
        assertNotNull(inglesEst)
        assertEquals(416_015.0, inglesEst?.amount ?: 0.0, 0.01)
    }

    @Test
    fun continuingEducation_calculatesHourlyPriceBasedOnUserCategory() {
        val state = viewModel.uiState.value
        val sstDiplomado = state.continuingEducationOffer.first { it.id == "EC-DIP-SST" } // 120 horas

        // Estudiante UTS ($18.910/h * 120h = $2.269.200)
        assertEquals(2_269_200.0, sstDiplomado.calculatePrice(UserCategory.ESTUDIANTE_UTS), 0.01)

        // Egresado UTS ($28.365/h * 120h = $3.403.800)
        assertEquals(3_403_800.0, sstDiplomado.calculatePrice(UserCategory.EGRESADO_UTS), 0.01)

        // Externo ($37.820/h * 120h = $4.538.400)
        assertEquals(4_538_400.0, sstDiplomado.calculatePrice(UserCategory.EXTERNO), 0.01)
    }

    @Test
    fun wellnessActivities_containVerifiedCategories() {
        val state = viewModel.uiState.value
        assertFalse(state.wellnessActivities.isEmpty())
        assertTrue(state.wellnessActivities.any { it.category.displayName.contains("Deporte") })
        assertTrue(state.wellnessActivities.any { it.category.displayName.contains("Salud") })
        assertTrue(state.wellnessActivities.any { it.category.displayName.contains("Cultura") })
    }

    @Test
    fun officialSources_containResolutionsAndOfficialUrls() {
        val state = viewModel.uiState.value
        assertFalse(state.officialSources.isEmpty())
        assertTrue(state.officialSources.any { it.sourceName.contains("Resolución UTS No. 02-1613") })
        assertTrue(state.officialSources.all { it.url.startsWith("http") })
    }
}
