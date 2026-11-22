package co.edu.uts.serviciosestudiantiles.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import co.edu.uts.serviciosestudiantiles.data.model.AcademicLevel
import co.edu.uts.serviciosestudiantiles.data.model.AcademicProgram
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.RegisteredStudent
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.data.repository.DefaultInstitutionalRepository
import co.edu.uts.serviciosestudiantiles.data.repository.InstitutionalRepository
import co.edu.uts.serviciosestudiantiles.data.repository.ProfileRepository
import co.edu.uts.serviciosestudiantiles.data.repository.StudentRegistryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.util.regex.Pattern

/**
 * ViewModel del Módulo 1: Registro y Perfil Académico UTS.
 *
 * Implementa:
 * - Regla de negocio: Generación automática de correo institucional simulado
 *   normalizando nombres y caracteres especiales (@uts.edu.co).
 * - Selección oficial de Facultad y Programa Académico estrictamente filtrado.
 * - Validación rigurosa de entradas antes de guardar.
 * - Encapsulación estricta de StateFlow (MutableStateFlow privado -> StateFlow público).
 * - Persistencia a través de ProfileRepository en base de datos Room.
 */
class ProfileViewModel(
    private val repository: ProfileRepository,
    private val institutionalRepository: InstitutionalRepository = DefaultInstitutionalRepository(),
    private val studentRegistryRepository: StudentRegistryRepository? = null
) : ViewModel() {

    // 1. Estado privado mutable (UDF)
    private val _uiState = MutableStateFlow(ProfileUiState())

    // 2. Estado público inmutable expuesto a Compose
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadExistingProfile()
    }

    /**
     * Carga el perfil previamente guardado si existe en el repositorio.
     */
    private fun loadExistingProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getProfileFlow().collect { profile ->
                if (profile != null) {
                    val matchingFaculty = profile.faculty
                    val availablePrograms = institutionalRepository.getProgramsByFaculty(matchingFaculty)
                    val matchingProgram = availablePrograms.firstOrNull {
                        it.name.equals(profile.programName, ignoreCase = true)
                    } ?: availablePrograms.firstOrNull()

                    val matchingCampus = Campus.fromCityName(profile.campus)
                    val matchingModality = Modality.fromDisplayName(profile.modality)
                    val matchingLevel = matchingProgram?.level ?: AcademicLevel.TECNOLOGICO

                    _uiState.update { current ->
                        current.copy(
                            fullName = profile.fullName,
                            studentCode = profile.studentCode,
                            documentNumber = profile.documentNumber,
                            selectedFaculty = matchingFaculty,
                            availablePrograms = availablePrograms,
                            selectedProgram = matchingProgram,
                            selectedCampus = matchingCampus,
                            selectedModality = matchingModality,
                            selectedLevel = matchingLevel,
                            generatedEmail = profile.institutionalEmail,
                            currentProfile = profile,
                            isSaved = true,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    /**
     * Evento: cambio de nombre completo.
     * Actualiza el correo institucional en tiempo real según la regla de negocio.
     */
    fun onFullNameChanged(newName: String) {
        val email = calculateInstitutionalEmail(newName, _uiState.value.studentCode)
        _uiState.update { current ->
            current.copy(
                fullName = newName,
                generatedEmail = email,
                nameError = if (newName.isNotBlank() && newName.trim().length < 3) {
                    "El nombre debe contener al menos 3 caracteres"
                } else null,
                successMessage = null
            )
        }
    }

    /**
     * Evento: cambio de código estudiantil.
     * Actualiza el correo institucional en tiempo real.
     */
    fun onStudentCodeChanged(newCode: String) {
        val filtered = newCode.filter { it.isLetterOrDigit() }
        val email = calculateInstitutionalEmail(_uiState.value.fullName, filtered)
        _uiState.update { current ->
            current.copy(
                studentCode = filtered,
                generatedEmail = email,
                codeError = if (filtered.isBlank()) "El código no puede estar vacío" else null,
                successMessage = null
            )
        }
    }

    /**
     * Evento: cambio de documento de identidad.
     */
    fun onDocumentNumberChanged(newDoc: String) {
        val filtered = newDoc.filter { it.isDigit() || it.isLetter() }
        _uiState.update { current ->
            current.copy(
                documentNumber = filtered,
                documentError = if (filtered.isBlank()) "El documento de identidad es obligatorio" else null,
                successMessage = null
            )
        }
    }

    /**
     * Evento: selección de facultad oficial.
     * Filtra los programas académicos disponibles exclusivamente para esta facultad.
     */
    fun onFacultySelected(faculty: Faculty) {
        val programsForFaculty = institutionalRepository.getProgramsByFaculty(faculty)
        val defaultProgram = programsForFaculty.firstOrNull()

        _uiState.update { current ->
            current.copy(
                selectedFaculty = faculty,
                availablePrograms = programsForFaculty,
                selectedProgram = defaultProgram,
                selectedLevel = defaultProgram?.level,
                selectedCampus = defaultProgram?.campuses?.firstOrNull() ?: Campus.BUCARAMANGA,
                selectedModality = defaultProgram?.modalities?.firstOrNull() ?: Modality.PRESENCIAL,
                facultyError = null,
                programError = null,
                successMessage = null
            )
        }
    }

    /**
     * Evento: selección de programa académico oficial.
     */
    fun onProgramSelected(program: AcademicProgram) {
        val validCampus = if (program.campuses.contains(_uiState.value.selectedCampus)) {
            _uiState.value.selectedCampus
        } else {
            program.campuses.firstOrNull() ?: Campus.BUCARAMANGA
        }

        val validModality = if (program.modalities.contains(_uiState.value.selectedModality)) {
            _uiState.value.selectedModality
        } else {
            program.modalities.firstOrNull() ?: Modality.PRESENCIAL
        }

        _uiState.update { current ->
            current.copy(
                selectedProgram = program,
                selectedLevel = program.level,
                selectedCampus = validCampus,
                selectedModality = validModality,
                programError = null,
                successMessage = null
            )
        }
    }

    /**
     * Evento: selección de sede física.
     */
    fun onCampusSelected(campus: Campus) {
        _uiState.update { it.copy(selectedCampus = campus, successMessage = null) }
    }

    /**
     * Evento: selección de modalidad.
     */
    fun onModalitySelected(modality: Modality) {
        _uiState.update { it.copy(selectedModality = modality, successMessage = null) }
    }

    /**
     * Evento: guardar perfil.
     * Ejecuta las validaciones de negocio antes de persistir la información en Room.
     */
    fun onSaveProfile() {
        val state = _uiState.value
        val nameTrimmed = state.fullName.trim()
        val codeTrimmed = state.studentCode.trim()
        val docTrimmed = state.documentNumber.trim()

        var hasError = false
        var nameErr: String? = null
        var codeErr: String? = null
        var docErr: String? = null
        var facultyErr: String? = null
        var programErr: String? = null

        if (nameTrimmed.isBlank()) {
            nameErr = "El nombre completo es obligatorio"
            hasError = true
        } else if (nameTrimmed.length < 3) {
            nameErr = "El nombre debe contener al menos 3 caracteres"
            hasError = true
        }

        if (codeTrimmed.isBlank()) {
            codeErr = "El código estudiantil es obligatorio"
            hasError = true
        }

        if (docTrimmed.isBlank()) {
            docErr = "El documento de identidad es obligatorio"
            hasError = true
        } else if (docTrimmed.length < 5) {
            docErr = "El documento debe contener al menos 5 dígitos"
            hasError = true
        }

        if (state.selectedFaculty == null) {
            facultyErr = "Debe seleccionar una facultad oficial"
            hasError = true
        }

        if (state.selectedProgram == null) {
            programErr = "Debe seleccionar un programa académico oficial"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    nameError = nameErr,
                    codeError = codeErr,
                    documentError = docErr,
                    facultyError = facultyErr,
                    programError = programErr,
                    successMessage = null
                )
            }
            return
        }

        val generatedEmail = calculateInstitutionalEmail(nameTrimmed, codeTrimmed)
        val selectedProgram = state.selectedProgram
        val campus = state.selectedCampus ?: Campus.BUCARAMANGA
        val modality = state.selectedModality ?: Modality.PRESENCIAL
        val level = state.selectedLevel ?: selectedProgram?.level ?: AcademicLevel.TECNOLOGICO

        val profile = StudentProfile(
            id = 1L,
            fullName = nameTrimmed,
            studentCode = codeTrimmed,
            documentNumber = docTrimmed,
            faculty = state.selectedFaculty!!,
            institutionalEmail = generatedEmail,
            programName = selectedProgram?.name ?: "",
            academicLevel = level.displayName,
            campus = campus.cityName,
            modality = modality.displayName,
            registeredAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Validación de duplicados contra el registro general si está disponible
            if (studentRegistryRepository != null) {
                val existingByCode = studentRegistryRepository.getStudentByCode(codeTrimmed)
                val existingByDoc = studentRegistryRepository.getStudentByDocument(docTrimmed)

                if (existingByCode != null && existingByDoc != null && existingByCode.id != existingByDoc.id) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            codeError = "El código ya pertenece a ${existingByCode.fullName}",
                            documentError = "El documento ya pertenece a ${existingByDoc.fullName}"
                        )
                    }
                    return@launch
                } else if (existingByCode != null && existingByDoc == null && existingByCode.documentNumber.isNotBlank() && existingByCode.documentNumber != docTrimmed) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            codeError = "El código $codeTrimmed ya está registrado con otro documento (${existingByCode.documentNumber})"
                        )
                    }
                    return@launch
                } else if (existingByDoc != null && existingByCode == null && existingByDoc.studentCode.isNotBlank() && existingByDoc.studentCode != codeTrimmed) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            documentError = "El documento $docTrimmed ya está registrado con otro código (${existingByDoc.studentCode})"
                        )
                    }
                    return@launch
                }
            }

            // Guardar perfil activo
            repository.saveProfile(profile)

            // Registrar automáticamente en el registro general de estudiantes
            if (studentRegistryRepository != null) {
                val existing = studentRegistryRepository.getStudentByCode(codeTrimmed)
                    ?: studentRegistryRepository.getStudentByDocument(docTrimmed)

                val registeredStudent = RegisteredStudent(
                    id = existing?.id ?: 0L,
                    fullName = nameTrimmed,
                    studentCode = codeTrimmed,
                    documentNumber = docTrimmed,
                    faculty = state.selectedFaculty!!,
                    programName = selectedProgram?.name ?: "",
                    academicLevel = level.displayName,
                    campus = campus.cityName,
                    modality = modality.displayName,
                    institutionalEmail = generatedEmail,
                    status = existing?.status ?: RegisteredStudent.STATUS_ACTIVO,
                    registeredAt = existing?.registeredAt ?: System.currentTimeMillis()
                )
                studentRegistryRepository.registerStudent(registeredStudent)
            }

            _uiState.update {
                it.copy(
                    fullName = nameTrimmed,
                    studentCode = codeTrimmed,
                    documentNumber = docTrimmed,
                    generatedEmail = generatedEmail,
                    currentProfile = profile,
                    isSaved = true,
                    isLoading = false,
                    nameError = null,
                    codeError = null,
                    documentError = null,
                    facultyError = null,
                    programError = null,
                    successMessage = "¡Perfil académico oficial y estudiante guardados exitosamente!"
                )
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    /**
     * Regla de Negocio Oficial: Generación automática de correo institucional.
     * Estructura: nombre.codigo@uts.edu.co
     */
    fun calculateInstitutionalEmail(name: String, code: String): String {
        val cleanName = normalizeString(name.trim())
            .replace("\\s+".toRegex(), ".")
            .replace("[^a-z0-9.]".toRegex(), "")
            .trim('.')

        val cleanCode = code.trim().lowercase()
            .replace("[^a-z0-9]".toRegex(), "")

        return when {
            cleanName.isNotBlank() && cleanCode.isNotBlank() -> "$cleanName.$cleanCode@uts.edu.co"
            cleanName.isNotBlank() -> "$cleanName@uts.edu.co"
            cleanCode.isNotBlank() -> "estudiante.$cleanCode@uts.edu.co"
            else -> "estudiante@uts.edu.co"
        }
    }

    private fun normalizeString(input: String): String {
        val nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("").lowercase()
    }

    companion object {
        fun provideFactory(
            repository: ProfileRepository,
            institutionalRepository: InstitutionalRepository = DefaultInstitutionalRepository(),
            studentRegistryRepository: StudentRegistryRepository? = null
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProfileViewModel(repository, institutionalRepository, studentRegistryRepository) as T
                }
            }
    }
}
