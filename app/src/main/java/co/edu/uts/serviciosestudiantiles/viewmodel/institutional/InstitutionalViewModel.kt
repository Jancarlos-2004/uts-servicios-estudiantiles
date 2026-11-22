package co.edu.uts.serviciosestudiantiles.viewmodel.institutional

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.edu.uts.serviciosestudiantiles.data.model.AcademicLevel
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.UserCategory
import co.edu.uts.serviciosestudiantiles.data.model.WellnessCategory
import co.edu.uts.serviciosestudiantiles.data.repository.DefaultInstitutionalRepository
import co.edu.uts.serviciosestudiantiles.data.repository.InstitutionalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel que gestiona la consulta del Catálogo de Oferta Académica Oficial,
 * Tarifas por Vigencia, Cursos de Educación Continua, Servicios de Bienestar
 * y Trazabilidad de Fuentes Normativas.
 */
class InstitutionalViewModel(
    private val institutionalRepository: InstitutionalRepository = DefaultInstitutionalRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(InstitutionalUiState())
    val uiState: StateFlow<InstitutionalUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val allPrograms = institutionalRepository.getAllPrograms()
        val t2027 = institutionalRepository.getTariffs(InstitutionalTariff.VIGENCIA_2027)
        val t2026 = institutionalRepository.getTariffs(InstitutionalTariff.VIGENCIA_2026)
        val contEd = institutionalRepository.getContinuingEducationOffer()
        val wellness = institutionalRepository.getWellnessActivities()
        val sources = institutionalRepository.getOfficialSources()

        _uiState.update { current ->
            current.copy(
                filteredPrograms = allPrograms,
                tariffs2027 = t2027,
                tariffs2026 = t2026,
                continuingEducationOffer = contEd,
                wellnessActivities = wellness,
                officialSources = sources
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onFacultyFilterSelected(faculty: Faculty?) {
        _uiState.update { it.copy(facultyFilter = faculty) }
        applyFilters()
    }

    fun onLevelFilterSelected(level: AcademicLevel?) {
        _uiState.update { it.copy(levelFilter = level) }
        applyFilters()
    }

    fun onModalityFilterSelected(modality: Modality?) {
        _uiState.update { it.copy(modalityFilter = modality) }
        applyFilters()
    }

    fun onCampusFilterSelected(campus: Campus?) {
        _uiState.update { it.copy(campusFilter = campus) }
        applyFilters()
    }

    fun onUserCategoryChanged(userCategory: UserCategory) {
        _uiState.update { it.copy(selectedUserCategory = userCategory) }
    }

    fun onWellnessCategoryFilterSelected(category: WellnessCategory?) {
        val filtered = institutionalRepository.getWellnessActivities(category)
        _uiState.update {
            it.copy(
                wellnessCategoryFilter = category,
                wellnessActivities = filtered
            )
        }
    }

    fun clearAllFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                facultyFilter = null,
                levelFilter = null,
                modalityFilter = null,
                campusFilter = null
            )
        }
        applyFilters()
    }

    private fun applyFilters() {
        val s = _uiState.value
        val filtered = institutionalRepository.filterPrograms(
            query = s.searchQuery,
            faculty = s.facultyFilter,
            level = s.levelFilter,
            modality = s.modalityFilter,
            campus = s.campusFilter
        )
        _uiState.update { it.copy(filteredPrograms = filtered) }
    }

    companion object {
        fun provideFactory(
            repository: InstitutionalRepository = DefaultInstitutionalRepository()
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return InstitutionalViewModel(repository) as T
                }
            }
    }
}
