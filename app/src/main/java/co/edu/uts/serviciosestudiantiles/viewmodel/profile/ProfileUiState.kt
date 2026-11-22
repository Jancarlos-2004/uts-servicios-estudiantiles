package co.edu.uts.serviciosestudiantiles.viewmodel.profile

import co.edu.uts.serviciosestudiantiles.data.model.AcademicLevel
import co.edu.uts.serviciosestudiantiles.data.model.AcademicProgram
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile

/**
 * Estado inmutable de la interfaz de usuario para el Módulo 1 (Perfil Académico).
 * Integra la selección oficial de facultad, programa, nivel, sede y modalidad.
 */
data class ProfileUiState(
    val fullName: String = "",
    val studentCode: String = "",
    val documentNumber: String = "",
    val selectedFaculty: Faculty? = null,
    val selectedProgram: AcademicProgram? = null,
    val selectedLevel: AcademicLevel? = null,
    val selectedCampus: Campus? = null,
    val selectedModality: Modality? = null,
    val availablePrograms: List<AcademicProgram> = emptyList(),
    val generatedEmail: String = "",
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val codeError: String? = null,
    val documentError: String? = null,
    val facultyError: String? = null,
    val programError: String? = null,
    val campusError: String? = null,
    val modalityError: String? = null,
    val successMessage: String? = null,
    val currentProfile: StudentProfile? = null
) {
    val isFormValid: Boolean
        get() = fullName.isNotBlank() &&
                studentCode.isNotBlank() &&
                documentNumber.isNotBlank() &&
                selectedFaculty != null &&
                selectedProgram != null &&
                nameError == null &&
                codeError == null &&
                documentError == null &&
                facultyError == null &&
                programError == null
}
