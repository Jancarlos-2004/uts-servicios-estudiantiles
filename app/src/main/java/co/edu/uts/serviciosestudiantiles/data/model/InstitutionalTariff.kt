package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Representa una Tarifa Oficial de Derechos Pecuniarios y Complementarios
 * de las Unidades Tecnológicas de Santander (UTS) clasificada por Vigencia.
 *
 * Fuente Vigencia 2027: Resolución UTS No. 02-1613 del 15 de septiembre de 2026.
 * Fuente Vigencia 2026: Resolución de Derechos Pecuniarios y Complementarios 2026 UTS.
 * Fecha de verificación: Noviembre 2026.
 */
data class InstitutionalTariff(
    val id: String,
    val conceptName: String,
    val amount: Double,
    val vigencia: String, // "2026" | "2027"
    val userType: String, // "Estudiante UTS" | "Egresado" | "Externo" | "Aspirante"
    val condition: String,
    val officialAct: String,
    val officialSourceUrl: String = "https://www.uts.edu.co/sitio/valores-pecuniarios/",
    val verificationDate: String = "15/09/2026"
) {
    companion object {
        const val VIGENCIA_2026 = "2026"
        const val VIGENCIA_2027 = "2027"

        /**
         * Catálogo Oficial de Tarifas Institucionales Vigentes.
         */
        val OFFICIAL_TARIFFS: List<InstitutionalTariff> = listOf(
            // =========================================================================
            // VIGENCIA 2027 (Resolución UTS No. 02-1613 del 15 de septiembre de 2026)
            // =========================================================================
            InstitutionalTariff(
                id = "TAR-2027-MAT-TEC",
                conceptName = "Matrícula Programas Tecnológicos",
                amount = 2_836_466.0,
                vigencia = VIGENCIA_2027,
                userType = "Estudiante UTS",
                condition = "Semestre ordinario para programas de nivel tecnológico",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-MAT-UNI",
                conceptName = "Matrícula Nivel Universitario",
                amount = 3_781_955.0,
                vigencia = VIGENCIA_2027,
                userType = "Estudiante UTS",
                condition = "Semestre ordinario para programas profesionales / ciclo universitario",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-ING-EST",
                conceptName = "Curso de Inglés para Estudiantes UTS",
                amount = 416_015.0,
                vigencia = VIGENCIA_2027,
                userType = "Estudiante UTS",
                condition = "Nivel modular en el Centro de Idiomas para estudiantes con matrícula activa",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-ING-EXT",
                conceptName = "Curso de Inglés para Externos",
                amount = 907_669.0,
                vigencia = VIGENCIA_2027,
                userType = "Externo",
                condition = "Público general / participantes sin matrícula académica activa en UTS",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-EC-EST",
                conceptName = "Educación Continua por Hora (Estudiantes)",
                amount = 18_910.0,
                vigencia = VIGENCIA_2027,
                userType = "Estudiante UTS",
                condition = "Valor por hora académica de capacitación en cursos y diplomados de extensión",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-EC-EGR",
                conceptName = "Educación Continua por Hora (Egresados)",
                amount = 28_365.0,
                vigencia = VIGENCIA_2027,
                userType = "Egresado",
                condition = "Valor por hora para egresados graduados de las UTS",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-EC-EXT",
                conceptName = "Educación Continua por Hora (Externos)",
                amount = 37_820.0,
                vigencia = VIGENCIA_2027,
                userType = "Externo",
                condition = "Valor por hora para profesionales y participantes externos",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-GRA-DER",
                conceptName = "Derechos de Grado",
                amount = 625_000.0,
                vigencia = VIGENCIA_2027,
                userType = "Estudiante UTS",
                condition = "Trámite de titulación académica para tecnólogo o universitario",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-CER-ACA",
                conceptName = "Certificados y Constancias Académicas",
                amount = 38_500.0,
                vigencia = VIGENCIA_2027,
                userType = "Estudiante UTS",
                condition = "Expedición oficial de certificados de notas, conducta y matrícula",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),
            InstitutionalTariff(
                id = "TAR-2027-VAC-CRE",
                conceptName = "Curso Intersemestral / Vacacional (por crédito)",
                amount = 198_500.0,
                vigencia = VIGENCIA_2027,
                userType = "Estudiante UTS",
                condition = "Valor de nivelación por cada crédito académico matriculado",
                officialAct = "Resolución UTS No. 02-1613 (15/09/2026)"
            ),

            // =========================================================================
            // VIGENCIA 2026 (Resolución de Derechos Pecuniarios 2026 UTS)
            // =========================================================================
            InstitutionalTariff(
                id = "TAR-2026-MAT-TEC",
                conceptName = "Matrícula Programas Tecnológicos",
                amount = 2_626_358.0,
                vigencia = VIGENCIA_2026,
                userType = "Estudiante UTS",
                condition = "Tarifa oficial para programas de nivel tecnológico vigencia 2026",
                officialAct = "Resolución Derechos Pecuniarios 2026 UTS"
            ),
            InstitutionalTariff(
                id = "TAR-2026-MAT-UNI",
                conceptName = "Matrícula Nivel Universitario",
                amount = 3_501_810.0,
                vigencia = VIGENCIA_2026,
                userType = "Estudiante UTS",
                condition = "Tarifa oficial para nivel profesional universitario vigencia 2026",
                officialAct = "Resolución Derechos Pecuniarios 2026 UTS"
            ),
            InstitutionalTariff(
                id = "TAR-2026-INS-ASP",
                conceptName = "Inscripción Aspirantes Pregrado",
                amount = 315_163.0,
                vigencia = VIGENCIA_2026,
                userType = "Aspirante",
                condition = "Gratuito para paso de técnico a tecnológico y de tecnológico a universitario",
                officialAct = "Resolución Derechos Pecuniarios 2026 UTS"
            ),
            InstitutionalTariff(
                id = "TAR-2026-ING-EST",
                conceptName = "Curso de Inglés para Estudiantes UTS",
                amount = 385_200.0,
                vigencia = VIGENCIA_2026,
                userType = "Estudiante UTS",
                condition = "Tarifa de inglés Centro de Idiomas vigencia 2026",
                officialAct = "Resolución Derechos Pecuniarios 2026 UTS"
            ),
            InstitutionalTariff(
                id = "TAR-2026-GRA-DER",
                conceptName = "Derechos de Grado",
                amount = 580_000.0,
                vigencia = VIGENCIA_2026,
                userType = "Estudiante UTS",
                condition = "Trámite de grado y diploma vigencia 2026",
                officialAct = "Resolución Derechos Pecuniarios 2026 UTS"
            ),
            InstitutionalTariff(
                id = "TAR-2026-CER-ACA",
                conceptName = "Certificados y Constancias Académicas",
                amount = 35_000.0,
                vigencia = VIGENCIA_2026,
                userType = "Estudiante UTS",
                condition = "Expedición de certificados oficiales vigencia 2026",
                officialAct = "Resolución Derechos Pecuniarios 2026 UTS"
            )
        )

        fun getTariffsByVigencia(vigencia: String): List<InstitutionalTariff> {
            return OFFICIAL_TARIFFS.filter { it.vigencia == vigencia }
        }
    }
}
