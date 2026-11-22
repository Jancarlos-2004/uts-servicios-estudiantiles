package co.edu.uts.serviciosestudiantiles.viewmodel.students

import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.RegisteredStudent

/**
 * Estado inmutable de la interfaz de usuario para el módulo de Estudiantes Registrados.
 */
data class StudentsListUiState(
    val allStudents: List<RegisteredStudent> = emptyList(),
    val filteredStudents: List<RegisteredStudent> = emptyList(),
    val searchQuery: String = "",
    val facultyFilter: Faculty? = null,
    val campusFilter: Campus? = null,
    val programFilter: String? = null,
    val statusFilter: String? = null,
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val studentForDetail: RegisteredStudent? = null,
    val studentToEdit: RegisteredStudent? = null,
    val studentToDelete: RegisteredStudent? = null,
    val isCreateDialogOpen: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
