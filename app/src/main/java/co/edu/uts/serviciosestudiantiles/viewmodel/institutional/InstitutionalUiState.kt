package co.edu.uts.serviciosestudiantiles.viewmodel.institutional

import co.edu.uts.serviciosestudiantiles.data.model.AcademicLevel
import co.edu.uts.serviciosestudiantiles.data.model.AcademicProgram
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.ContinuingEducationCourse
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalSource
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.UserCategory
import co.edu.uts.serviciosestudiantiles.data.model.WellnessActivity
import co.edu.uts.serviciosestudiantiles.data.model.WellnessCategory

/**
 * Estado inmutable para la consulta institucional (Oferta Académica, Tarifas por Vigencia,
 * Educación Continua, Bienestar Institucional y Fuentes Oficiales).
 */
data class InstitutionalUiState(
    val filteredPrograms: List<AcademicProgram> = emptyList(),
    val searchQuery: String = "",
    val facultyFilter: Faculty? = null,
    val levelFilter: AcademicLevel? = null,
    val modalityFilter: Modality? = null,
    val campusFilter: Campus? = null,
    val tariffs2027: List<InstitutionalTariff> = emptyList(),
    val tariffs2026: List<InstitutionalTariff> = emptyList(),
    val continuingEducationOffer: List<ContinuingEducationCourse> = emptyList(),
    val selectedUserCategory: UserCategory = UserCategory.ESTUDIANTE_UTS,
    val wellnessActivities: List<WellnessActivity> = emptyList(),
    val wellnessCategoryFilter: WellnessCategory? = null,
    val officialSources: List<InstitutionalSource> = emptyList(),
    val isLoading: Boolean = false
)
