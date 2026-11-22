package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Modelo de dominio que representa el perfil académico de un estudiante en el sistema UTS.
 * Se encuentra vinculado a la estructura institucional oficial de facultades, programas,
 * sedes físicas y modalidades.
 *
 * @param id Identificador único (por defecto 1 para el usuario activo en sesión local)
 * @param fullName Nombre completo del estudiante
 * @param studentCode Código estudiantil asignado por la institución
 * @param faculty Facultad oficial a la que pertenece
 * @param institutionalEmail Correo institucional simulado (nombre.codigo@uts.edu.co)
 * @param programName Nombre oficial del programa académico matriculado
 * @param academicLevel Nivel de formación (Nivel Tecnológico o Universitario)
 * @param campus Sede física donde cursa estudios (Bucaramanga, Piedecuesta, Vélez, Barrancabermeja)
 * @param modality Modalidad de estudio (Presencial, Virtual)
 * @param registeredAt Marca de tiempo del registro
 */
data class StudentProfile(
    val id: Long = 1L,
    val fullName: String,
    val studentCode: String,
    val documentNumber: String = "",
    val faculty: Faculty,
    val institutionalEmail: String,
    val programName: String = "",
    val academicLevel: String = "",
    val campus: String = "",
    val modality: String = "",
    val registeredAt: Long = System.currentTimeMillis()
) {
    val isValid: Boolean
        get() = fullName.isNotBlank() && studentCode.isNotBlank() && institutionalEmail.isNotBlank()
}
