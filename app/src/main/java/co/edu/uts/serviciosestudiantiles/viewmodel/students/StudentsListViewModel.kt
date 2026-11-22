package co.edu.uts.serviciosestudiantiles.viewmodel.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.RegisteredStudent
import co.edu.uts.serviciosestudiantiles.data.repository.DefaultInstitutionalRepository
import co.edu.uts.serviciosestudiantiles.data.repository.InstitutionalRepository
import co.edu.uts.serviciosestudiantiles.data.repository.StudentRegistryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para el módulo de Estudiantes Registrados.
 * Implementa arquitectura MVVM + UDF + StateFlow para consulta, búsqueda, filtros y CRUD.
 */
class StudentsListViewModel(
    private val repository: StudentRegistryRepository,
    val institutionalRepository: InstitutionalRepository = DefaultInstitutionalRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentsListUiState(isLoading = true))
    val uiState: StateFlow<StudentsListUiState> = _uiState.asStateFlow()

    init {
        loadStudents()
    }

    private fun loadStudents() {
        viewModelScope.launch {
            repository.getAllStudentsFlow().collect { students ->
                _uiState.update { current ->
                    val filtered = applyFilters(
                        list = students,
                        search = current.searchQuery,
                        faculty = current.facultyFilter,
                        campus = current.campusFilter,
                        program = current.programFilter,
                        status = current.statusFilter
                    )
                    current.copy(
                        allStudents = students,
                        filteredStudents = filtered,
                        totalCount = students.size,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { current ->
            val filtered = applyFilters(
                list = current.allStudents,
                search = query,
                faculty = current.facultyFilter,
                campus = current.campusFilter,
                program = current.programFilter,
                status = current.statusFilter
            )
            current.copy(searchQuery = query, filteredStudents = filtered)
        }
    }

    fun onFacultyFilterSelected(faculty: Faculty?) {
        _uiState.update { current ->
            val filtered = applyFilters(
                list = current.allStudents,
                search = current.searchQuery,
                faculty = faculty,
                campus = current.campusFilter,
                program = current.programFilter,
                status = current.statusFilter
            )
            current.copy(facultyFilter = faculty, filteredStudents = filtered)
        }
    }

    fun onCampusFilterSelected(campus: Campus?) {
        _uiState.update { current ->
            val filtered = applyFilters(
                list = current.allStudents,
                search = current.searchQuery,
                faculty = current.facultyFilter,
                campus = campus,
                program = current.programFilter,
                status = current.statusFilter
            )
            current.copy(campusFilter = campus, filteredStudents = filtered)
        }
    }

    fun onProgramFilterSelected(programName: String?) {
        _uiState.update { current ->
            val filtered = applyFilters(
                list = current.allStudents,
                search = current.searchQuery,
                faculty = current.facultyFilter,
                campus = current.campusFilter,
                program = programName,
                status = current.statusFilter
            )
            current.copy(programFilter = programName, filteredStudents = filtered)
        }
    }

    fun onStatusFilterSelected(status: String?) {
        _uiState.update { current ->
            val filtered = applyFilters(
                list = current.allStudents,
                search = current.searchQuery,
                faculty = current.facultyFilter,
                campus = current.campusFilter,
                program = current.programFilter,
                status = status
            )
            current.copy(statusFilter = status, filteredStudents = filtered)
        }
    }

    fun onSelectStudentForDetail(student: RegisteredStudent?) {
        _uiState.update { it.copy(studentForDetail = student) }
    }

    fun onOpenCreateDialog() {
        _uiState.update { it.copy(isCreateDialogOpen = true, errorMessage = null) }
    }

    fun onCloseCreateDialog() {
        _uiState.update { it.copy(isCreateDialogOpen = false, errorMessage = null) }
    }

    fun onCreateStudent(student: RegisteredStudent) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.registerStudent(student)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isCreateDialogOpen = false,
                            successMessage = "¡Estudiante ${student.fullName} registrado exitosamente!"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Error al registrar estudiante"
                        )
                    }
                }
            )
        }
    }

    fun onOpenEditDialog(student: RegisteredStudent) {
        _uiState.update { it.copy(studentToEdit = student, errorMessage = null) }
    }

    fun onCloseEditDialog() {
        _uiState.update { it.copy(studentToEdit = null, errorMessage = null) }
    }

    fun onUpdateStudent(student: RegisteredStudent) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.updateStudent(student)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            studentToEdit = null,
                            studentForDetail = if (it.studentForDetail?.id == student.id) student else it.studentForDetail,
                            successMessage = "¡Información del estudiante actualizada exitosamente!"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Error al actualizar estudiante"
                        )
                    }
                }
            )
        }
    }

    fun onConfirmDeleteStudent(student: RegisteredStudent) {
        _uiState.update { it.copy(studentToDelete = student) }
    }

    fun onDismissDelete() {
        _uiState.update { it.copy(studentToDelete = null) }
    }

    fun onExecuteDelete() {
        val student = _uiState.value.studentToDelete ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.deleteStudent(student.id)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            studentToDelete = null,
                            studentForDetail = if (it.studentForDetail?.id == student.id) null else it.studentForDetail,
                            successMessage = "Estudiante ${student.fullName} eliminado del registro."
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            studentToDelete = null,
                            errorMessage = error.localizedMessage ?: "Error al eliminar estudiante"
                        )
                    }
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }

    private fun applyFilters(
        list: List<RegisteredStudent>,
        search: String,
        faculty: Faculty?,
        campus: Campus?,
        program: String?,
        status: String?
    ): List<RegisteredStudent> {
        val searchClean = search.trim().lowercase()

        return list.filter { student ->
            val matchesSearch = searchClean.isBlank() ||
                    student.fullName.lowercase().contains(searchClean) ||
                    student.studentCode.lowercase().contains(searchClean) ||
                    student.documentNumber.lowercase().contains(searchClean)

            val matchesFaculty = faculty == null || student.faculty == faculty
            val matchesCampus = campus == null || student.campus.equals(campus.cityName, ignoreCase = true)
            val matchesProgram = program.isNullOrBlank() || student.programName.equals(program, ignoreCase = true)
            val matchesStatus = status.isNullOrBlank() || student.status.equals(status, ignoreCase = true)

            matchesSearch && matchesFaculty && matchesCampus && matchesProgram && matchesStatus
        }.sortedByDescending { it.registeredAt }
    }

    companion object {
        fun provideFactory(
            repository: StudentRegistryRepository,
            institutionalRepository: InstitutionalRepository = DefaultInstitutionalRepository()
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StudentsListViewModel(repository, institutionalRepository) as T
                }
            }
    }
}
