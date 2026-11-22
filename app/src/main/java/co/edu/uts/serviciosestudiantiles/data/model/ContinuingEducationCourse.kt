package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Oferta oficial de Educación Continua, Extensión y Capacitación de las UTS.
 * Fuente: Oficina de Extensión y Educación Continua UTS (Campus Bucaramanga, Edificio A, piso 7).
 * Tarifas por hora calculadas con base en la Resolución UTS No. 02-1613 del 15 de septiembre de 2026:
 * - Estudiante UTS: $18.910 COP/hora
 * - Egresado UTS: $28.365 COP/hora
 * - Externo: $37.820 COP/hora
 *
 * Fecha de verificación: Noviembre 2026.
 */
data class ContinuingEducationCourse(
    val id: String,
    val name: String,
    val type: String, // "Diplomado" | "Curso Taller" | "Seminario" | "Congreso"
    val durationHours: Int,
    val modality: Modality,
    val description: String,
    val targetAudience: String,
    val requirements: String,
    val officialUrl: String = "https://www.uts.edu.co/sitio/educacion-continua/",
    val contactEmail: String = "educacioncontinua@correo.uts.edu.co"
) {
    /**
     * Calcula el costo oficial para el tipo de participante según la tarifa horaria
     * reglamentada en la Resolución No. 02-1613 de 2026 para la vigencia 2027.
     */
    fun calculatePrice(userType: UserCategory): Double {
        val hourlyRate = when (userType) {
            UserCategory.ESTUDIANTE_UTS -> 18_910.0
            UserCategory.EGRESADO_UTS -> 28_365.0
            UserCategory.EXTERNO -> 37_820.0
        }
        return durationHours * hourlyRate
    }

    companion object {
        val OFFICIAL_CONTINUING_ED_OFFER: List<ContinuingEducationCourse> = listOf(
            ContinuingEducationCourse(
                id = "EC-DIP-SST",
                name = "Diplomado en Seguridad y Salud en el Trabajo (SG-SST)",
                type = "Diplomado",
                durationHours = 120,
                modality = Modality.VIRTUAL,
                description = "Formación integral en diseño, implementación y auditoría de Sistemas de Gestión de Seguridad y Salud en el Trabajo conforme al Decreto 1072 y Resolución 0312.",
                targetAudience = "Estudiantes de ingeniería, tecnólogos ambientales, industriales y profesionales del sector productivo.",
                requirements = "Título técnico, tecnológico o ser estudiante activo de últimos semestres en áreas afines."
            ),
            ContinuingEducationCourse(
                id = "EC-DIP-TH",
                name = "Diplomado en Gestión Estratégica del Talento Humano",
                type = "Diplomado",
                durationHours = 120,
                modality = Modality.VIRTUAL,
                description = "Modelos modernos de atracción, desarrollo de competencias, clima organizacional y legislación laboral colombiana.",
                targetAudience = "Estudiantes y egresados de Gestión Empresarial, Administración de Empresas y áreas socioeconómicas.",
                requirements = "Copia de documento de identidad y fotocopia de carnet estudiantil o diploma."
            ),
            ContinuingEducationCourse(
                id = "EC-CUR-HAC",
                name = "Curso Taller de Hacking Ético y Ciberseguridad Defensiva",
                type = "Curso Taller",
                durationHours = 40,
                modality = Modality.PRESENCIAL,
                description = "Prácticas de laboratorio en análisis de vulnerabilidades, auditoría de redes, pentesting y protección de infraestructura de servidores.",
                targetAudience = "Estudiantes de Sistemas, Telecomunicaciones e ingenieros de soporte TI.",
                requirements = "Conocimientos previos en redes TCP/IP y comandos básicos de Linux."
            ),
            ContinuingEducationCourse(
                id = "EC-CUR-AND",
                name = "Curso de Desarrollo Móvil con Android, Kotlin y Jetpack Compose",
                type = "Curso Taller",
                durationHours = 40,
                modality = Modality.PRESENCIAL,
                description = "Construcción de interfaces modernas reactivas, arquitectura MVVM, persistencia en Room y consumo de APIs REST.",
                targetAudience = "Desarrolladores junior, tecnólogos en informática y estudiantes de ingeniería de sistemas.",
                requirements = "Bases sólidas de programación orientada a objetos en Java o Kotlin."
            ),
            ContinuingEducationCourse(
                id = "EC-SEM-TRIB",
                name = "Seminario de Actualización Tributaria y Cierre Contable",
                type = "Seminario",
                durationHours = 30,
                modality = Modality.PRESENCIAL,
                description = "Análisis de las reformas tributarias vigentes, retención en la fuente, IVA, impuesto de renta y aplicaciones prácticas de las NIIF.",
                targetAudience = "Contadores públicos, tecnólogos en información contable y auxiliares financieros.",
                requirements = "Abierto a la comunidad académica y empresarial interesada."
            ),
            ContinuingEducationCourse(
                id = "EC-IDI-ING",
                name = "Módulo de Inglés Comunicativo B1 - Centro de Idiomas UTS",
                type = "Curso de Idiomas",
                durationHours = 60,
                modality = Modality.PRESENCIAL,
                description = "Desarrollo de competencias lingüísticas en escucha, lectura, escritura y expresión oral bajo el Marco Común Europeo.",
                targetAudience = "Estudiantes UTS con requisito de grado en segunda lengua y externos interesados.",
                requirements = "Presentar prueba clasificatoria o haber aprobado el nivel A2."
            )
        )
    }
}

enum class UserCategory(val displayName: String) {
    ESTUDIANTE_UTS("Estudiante Activo UTS"),
    EGRESADO_UTS("Egresado Graduado UTS"),
    EXTERNO("Participante Externo");

    companion object {
        fun fromDisplayName(name: String?): UserCategory {
            return entries.firstOrNull { it.displayName.equals(name?.trim(), ignoreCase = true) } ?: ESTUDIANTE_UTS
        }
    }
}
