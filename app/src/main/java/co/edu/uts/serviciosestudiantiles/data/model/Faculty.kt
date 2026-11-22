package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Representa las dos facultades académicas oficiales de las
 * Unidades Tecnológicas de Santander (UTS) según el Estatuto General
 * y la estructura institucional publicada en el portal oficial: https://www.uts.edu.co/
 *
 * Fuente: Portal Institucional UTS - Secciones de Facultades Oficiales.
 * Fecha de verificación: Noviembre 2026.
 */
enum class Faculty(
    val displayName: String,
    val shortName: String,
    val description: String
) {
    CIENCIAS_NATURALES_E_INGENIERIAS(
        displayName = "Facultad de Ciencias Naturales e Ingenierías",
        shortName = "Ingenierías",
        description = "Programas tecnológicos y de ingeniería en sistemas, ambiental, electricidad, electrónica, electromecánica, industrial, telecomunicaciones, topografía y recursos naturales."
    ),
    CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES(
        displayName = "Facultad de Ciencias Socioeconómicas y Empresariales",
        shortName = "Ciencias Socioeconómicas",
        description = "Programas tecnológicos y universitarios en administración de empresas, contaduría pública, mercadeo, finanzas, moda, deporte, negocios internacionales y economía."
    );

    companion object {
        // Alias de compatibilidad hacia atrás para código preexistente
        val INGENIERIAS: Faculty get() = CIENCIAS_NATURALES_E_INGENIERIAS
        val CIENCIAS_SOCIOECONOMICAS: Faculty get() = CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES

        /**
         * Resuelve la facultad a partir del nombre completo, nombre corto o
         * cadenas almacenadas en versiones previas de la base de datos local.
         */
        fun fromDisplayName(name: String?): Faculty? {
            if (name.isNullOrBlank()) return null
            val clean = name.trim().lowercase()
            return when {
                clean.contains("ingenier") || clean.contains("naturales") -> CIENCIAS_NATURALES_E_INGENIERIAS
                clean.contains("socioecon") || clean.contains("empresarial") -> CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES
                else -> entries.find {
                    it.displayName.equals(name, ignoreCase = true) ||
                    it.shortName.equals(name, ignoreCase = true)
                }
            }
        }
    }
}
