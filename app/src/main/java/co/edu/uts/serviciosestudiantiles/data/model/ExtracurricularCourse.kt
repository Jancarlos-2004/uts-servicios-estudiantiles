package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Cursos extracurriculares obligatorios con sus costos base oficiales:
 * - Inglés Técnico: $350.000 COP
 * - Programación Web: $420.000 COP
 * - Ciberseguridad: $500.000 COP
 */
enum class ExtracurricularCourse(
    val title: String,
    val basePrice: Double,
    val description: String
) {
    INGLES_TECNICO(
        title = "Inglés Técnico",
        basePrice = 350_000.0,
        description = "Competencias comunicativas en inglés para entornos profesionales y tecnológicos."
    ),
    PROGRAMACION_WEB(
        title = "Programación Web",
        basePrice = 420_000.0,
        description = "Desarrollo moderno frontend y backend con tecnologías web actuales."
    ),
    CIBERSEGURIDAD(
        title = "Ciberseguridad",
        basePrice = 500_000.0,
        description = "Fundamentos de protección de redes, seguridad de datos y ethical hacking."
    );

    companion object {
        fun fromTitle(title: String): ExtracurricularCourse {
            return entries.firstOrNull { it.title.equals(title, ignoreCase = true) } ?: PROGRAMACION_WEB
        }
    }
}

