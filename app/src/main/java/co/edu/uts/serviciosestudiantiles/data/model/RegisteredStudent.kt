package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Modelo de dominio que representa a un estudiante registrado en el sistema general UTS.
 * Permite la gestión completa de matrícula, consulta general, estados y trazabilidad.
 */
data class RegisteredStudent(
    val id: Long = 0L,
    val fullName: String,
    val studentCode: String,
    val documentNumber: String,
    val faculty: Faculty,
    val programName: String,
    val academicLevel: String,
    val campus: String,
    val modality: String,
    val institutionalEmail: String,
    val status: String = STATUS_ACTIVO,
    val registeredAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val STATUS_ACTIVO = "Activo"
        const val STATUS_MATRICULADO = "Matriculado"
        const val STATUS_INACTIVO = "Inactivo"

        val ALL_STATUSES = listOf(STATUS_ACTIVO, STATUS_MATRICULADO, STATUS_INACTIVO)
    }

    val isValid: Boolean
        get() = fullName.isNotBlank() &&
                studentCode.isNotBlank() &&
                documentNumber.isNotBlank() &&
                programName.isNotBlank()
}
