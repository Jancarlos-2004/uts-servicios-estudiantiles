package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Catálogo Oficial de Beneficios, Estímulos, Descuentos y Mecanismos de Financiación de la UTS.
 *
 * Fuentes oficiales:
 * - Reglamento de Estímulos y Distinciones UTS (Acuerdo No. 01-035 de 2019 y sus modificaciones).
 * - Ley 403 de 1997 y Ley 815 de 2003 (Descuento por Certificado Electoral).
 * - Política de Gratuidad "Puedo Estudiar" del Ministerio de Educación Nacional y UTS.
 * - Convenio Marco Institucional UTS - ICETEX.
 * Fecha de verificación: Noviembre 2026.
 */
data class InstitutionalBenefit(
    val id: String,
    val name: String,
    val discountPercentage: Double, // 0.0 a 1.0 (ej. 0.50 = 50%)
    val appliesToMatriculaOnly: Boolean = true,
    val legalBasis: String,
    val requirements: String,
    val population: String,
    val officialUrl: String = "https://www.uts.edu.co/sitio/normatividad/"
) {
    companion object {
        val NINGUNO = InstitutionalBenefit(
            id = "BEN-NINGUNO",
            name = "Tarifa Plena Ordinaria (Sin Beneficio)",
            discountPercentage = 0.0,
            legalBasis = "Tarifas plenas oficiales de derechos pecuniarios UTS",
            requirements = "Aplica a estudiantes regulares sin estímulos vigentes acreditados",
            population = "Estudiantes regulares matriculados"
        )

        val BECA_EXCELENCIA_50 = InstitutionalBenefit(
            id = "BEN-BECA-50",
            name = "Beca de Excelencia Académica (50% Descuento)",
            discountPercentage = 0.50,
            legalBasis = "Reglamento de Estímulos y Distinciones UTS (Acuerdo No. 01-035 de 2019)",
            requirements = "Promedio ponderado acumulado igual o superior a 4.00, estar nivelado, haber aprobado el 100% de asignaturas del semestre previo, sin pérdidas de materias, sin validaciones ni sanciones disciplinarias. Asignada al 1% de los mejores promedios del programa.",
            population = "Estudiantes con rendimiento de honor (1% superior)"
        )

        val BECA_EXCELENCIA_25 = InstitutionalBenefit(
            id = "BEN-BECA-25",
            name = "Beca de Excelencia Académica (25% Descuento)",
            discountPercentage = 0.25,
            legalBasis = "Reglamento de Estímulos y Distinciones UTS (Acuerdo No. 01-035 de 2019)",
            requirements = "Promedio acumulado >= 4.00, nivelado y sin sanciones. Asignada al segundo 1% de los mejores promedios del programa.",
            population = "Estudiantes destacados del programa (segundo 1%)"
        )

        val DESCUENTO_ELECTORAL_10 = InstitutionalBenefit(
            id = "BEN-ELECTORAL-10",
            name = "Descuento por Votación Electoral (10%)",
            discountPercentage = 0.10,
            legalBasis = "Ley 403 de 1997, artículo 2, numeral 5 / Ley 815 de 2003",
            requirements = "Presentación del certificado electoral de las últimas elecciones oficiales nacionales ante la División Financiera en las fechas del calendario académico.",
            population = "Estudiantes sufragantes en comicios electorales colombianos"
        )

        val POLITICA_GRATUIDAD_100 = InstitutionalBenefit(
            id = "BEN-GRATUIDAD-100",
            name = "Política de Gratuidad 'Puedo Estudiar' (100% Matrícula Neta)",
            discountPercentage = 1.00,
            legalBasis = "Ley 2307 de 2023 / Decreto 2271 de 2023 / MinEducación y UTS",
            requirements = "Pertenecer a estratos 1, 2 o 3, o estar registrado en Sisbén IV (grupos A, B o C), ser colombiano(a), no poseer título profesional universitario previo y encontrarse en el rango de edad y créditos autorizados.",
            population = "Estudiantes en condición de vulnerabilidad socioeconómica focalizada"
        )

        val OFFICIAL_BENEFITS: List<InstitutionalBenefit> = listOf(
            NINGUNO,
            BECA_EXCELENCIA_50,
            BECA_EXCELENCIA_25,
            DESCUENTO_ELECTORAL_10,
            POLITICA_GRATUIDAD_100
        )
    }
}

/**
 * Información Institucional sobre la Financiación con ICETEX.
 * Aclaración Oficial: ICETEX es una entidad del Estado de crédito educativo.
 * La UTS NO aplica ningún recargo administrativo del 5% a las liquidaciones financiadas con ICETEX.
 */
data class IcetexFundingInfo(
    val title: String = "Línea de Crédito Educativo ICETEX - UTS",
    val description: String = "Modalidad de pago financiado mediante crédito condonable o reembolsable con ICETEX (Líneas Tú Eliges, Fondos en Administración).",
    val surchargePercentage: Double = 0.0, // OFICIAL: 0% de recargo institucional
    val note: String = "La UTS no cobra recargo alguno por concepto de pago con crédito ICETEX. El valor a liquidar es el valor neto oficial de matrícula.",
    val officialUrl: String = "https://www.uts.edu.co/sitio/icetex/"
)
