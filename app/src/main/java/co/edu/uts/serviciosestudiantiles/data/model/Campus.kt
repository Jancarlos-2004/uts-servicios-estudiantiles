package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Sedes y campus físicos oficiales de las Unidades Tecnológicas de Santander (UTS).
 * Fuente: Portal Institucional UTS (www.uts.edu.co/sitio/sedes/)
 * Fecha de verificación: Noviembre 2026.
 */
enum class Campus(
    val cityName: String,
    val address: String,
    val department: String = "Santander"
) {
    BUCARAMANGA(
        cityName = "Bucaramanga",
        address = "Calle de los Estudiantes # 9-82, Ciudadela Real de Minas (Campus Central)"
    ),
    PIEDECUESTA(
        cityName = "Piedecuesta",
        address = "Sede Guatiguará / Convenio Académico Regional"
    ),
    VELEZ(
        cityName = "Vélez",
        address = "Carrera 5 # 7-42 (Sede Regional Provincia de Vélez)"
    ),
    BARRANCABERMEJA(
        cityName = "Barrancabermeja",
        address = "Calle 50 # 18-20 (Sede Regional Magdalena Medio)"
    );

    companion object {
        fun fromCityName(name: String?): Campus {
            return entries.firstOrNull { it.cityName.equals(name?.trim(), ignoreCase = true) } ?: BUCARAMANGA
        }
    }
}

/**
 * Modalidades oficiales de formación académica en las UTS.
 * El Campus Virtual NO es una sede física, sino la modalidad mediada por tecnologías
 * a través de la plataforma institucional UTS Virtual (Canvas LMS).
 */
enum class Modality(val displayName: String, val isOnline: Boolean) {
    PRESENCIAL("Presencial", isOnline = false),
    VIRTUAL("Virtual (UTS Virtual)", isOnline = true),
    DISTANCIA("A Distancia", isOnline = false);

    companion object {
        fun fromDisplayName(name: String?): Modality {
            return entries.firstOrNull { it.displayName.equals(name?.trim(), ignoreCase = true) } ?: PRESENCIAL
        }
    }
}

/**
 * Niveles de formación académica de la UTS organizados en Ciclos Propedéuticos.
 */
enum class AcademicLevel(val displayName: String, val semestersTypical: Int) {
    TECNOLOGICO("Nivel Tecnológico", 6),
    UNIVERSITARIO("Nivel Universitario / Profesional", 4),
    ESPECIALIZACION("Especialización Tecnológica", 2);

    companion object {
        fun fromDisplayName(name: String?): AcademicLevel {
            return entries.firstOrNull { it.displayName.equals(name?.trim(), ignoreCase = true) } ?: TECNOLOGICO
        }
    }
}
