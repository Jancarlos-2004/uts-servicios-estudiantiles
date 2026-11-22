package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Registro Institucional de Fuentes Oficiales y Trazabilidad Normativa.
 * Garantiza que cada conjunto de datos del sistema provenga de fuentes verificables de la UTS.
 */
data class InstitutionalSource(
    val id: String,
    val sourceName: String,
    val issuingBody: String,
    val url: String,
    val consultationDate: String,
    val publicationDate: String,
    val vigencia: String,
    val supportedDataType: String,
    val legalSummary: String
) {
    companion object {
        val OFFICIAL_SOURCES: List<InstitutionalSource> = listOf(
            InstitutionalSource(
                id = "SRC-RES-2027",
                sourceName = "Resolución UTS No. 02-1613 del 15 de septiembre de 2026",
                issuingBody = "Rectoría y Consejo Directivo de las Unidades Tecnológicas de Santander",
                url = "https://www.uts.edu.co/sitio/valores-pecuniarios/",
                consultationDate = "19/11/2026",
                publicationDate = "15/09/2026",
                vigencia = "2027",
                supportedDataType = "Derechos Pecuniarios y Tarifas 2027 (Matrícula, Idiomas, Educación Continua, Grados, Certificados)",
                legalSummary = "Fija los valores de los derechos pecuniarios y complementarios aplicables para el periodo académico 2027 en todos los campus y modalidades de la institución."
            ),
            InstitutionalSource(
                id = "SRC-RES-2026",
                sourceName = "Resolución de Derechos Pecuniarios y Complementarios 2026",
                issuingBody = "Consejo Directivo UTS",
                url = "https://www.uts.edu.co/sitio/valores-pecuniarios/",
                consultationDate = "19/11/2026",
                publicationDate = "Diciembre 2025",
                vigencia = "2026",
                supportedDataType = "Derechos Pecuniarios 2026 (Matrícula Tecnología $2.626.358, Universitario $3.501.810, Inscripciones)",
                legalSummary = "Establece las tarifas académicas y administrativas vigentes durante el año lectivo 2026."
            ),
            InstitutionalSource(
                id = "SRC-OFERTA-ACAD",
                sourceName = "Catálogo Oficial de Oferta Académica y Registros Calificados SNIES",
                issuingBody = "Vicerrectoría Académica UTS / Ministerio de Educación Nacional",
                url = "https://www.uts.edu.co/sitio/oferta-academica-2/",
                consultationDate = "19/11/2026",
                publicationDate = "Actualización permanente",
                vigencia = "Vigente 2026 - 2027",
                supportedDataType = "Programas Académicos, Facultades Oficiales, Códigos SNIES, Sedes Físicas y Modalidades",
                legalSummary = "Estructura académica oficial articulada en Ciclos Propedéuticos (Tecnología y Nivel Universitario)."
            ),
            InstitutionalSource(
                id = "SRC-ESTIMULOS",
                sourceName = "Reglamento de Estímulos y Distinciones (Acuerdo No. 01-035 de 2019 y modif.)",
                issuingBody = "Consejo Directivo UTS",
                url = "https://www.uts.edu.co/sitio/normatividad/",
                consultationDate = "19/11/2026",
                publicationDate = "2019 - Vigente",
                vigencia = "Institucional Permanente",
                supportedDataType = "Beca por Excelencia Académica (50% y 25%), Excelencia Deportiva, Cultural y Emprendimiento",
                legalSummary = "Reglamenta los requisitos y porcentajes de descuento sobre matrícula para el 3% de los mejores promedios acumulados (>= 4.00, nivelados y sin sanciones)."
            ),
            InstitutionalSource(
                id = "SRC-LEY-ELECTORAL",
                sourceName = "Ley 403 de 1997 (Art. 2 Numeral 5) y Ley 815 de 2003",
                issuingBody = "Congreso de la República de Colombia / Aplicado en UTS",
                url = "https://www.funcionpublica.gov.co/eva/gestornormativo/norma.php?i=344",
                consultationDate = "19/11/2026",
                publicationDate = "Vigente",
                vigencia = "Nacional Permanente",
                supportedDataType = "Descuento del 10% en el valor de la matrícula por certificado electoral",
                legalSummary = "Establece el derecho a un descuento del diez por ciento (10%) en el costo de la matrícula en instituciones oficiales de educación superior para estudiantes que acrediten haber votado."
            ),
            InstitutionalSource(
                id = "SRC-GRATUIDAD",
                sourceName = "Política de Estado de Gratuidad en la Educación Superior 'Puedo Estudiar'",
                issuingBody = "Ministerio de Educación Nacional (Ley 2307 / Decreto 2271 de 2023) y UTS",
                url = "https://www.uts.edu.co/sitio/gratuidad/",
                consultationDate = "19/11/2026",
                publicationDate = "2024 - 2026",
                vigencia = "Vigente",
                supportedDataType = "Cobertura del 100% en el valor neto de la matrícula para estratos 1, 2 y 3 / Sisbén IV (A, B, C)",
                legalSummary = "Beneficio de gratuidad gubernamental sin cobro de matrícula para estudiantes vulnerables; no cubre derechos complementarios."
            ),
            InstitutionalSource(
                id = "SRC-ED-CONTINUA",
                sourceName = "Portal de Extensión y Educación Continua UTS",
                issuingBody = "Oficina de Extensión Institucional UTS",
                url = "https://www.uts.edu.co/sitio/educacion-continua/",
                consultationDate = "19/11/2026",
                publicationDate = "2026",
                vigencia = "Vigente",
                supportedDataType = "Oferta de diplomados, cursos y seminarios extracurriculares",
                legalSummary = "Cursos certificados y diplomados con tarifas por hora reglamentadas según categoría del usuario."
            ),
            InstitutionalSource(
                id = "SRC-BIENESTAR",
                sourceName = "Portal de Bienestar Institucional UTS",
                issuingBody = "Dirección de Bienestar Institucional UTS",
                url = "https://www.uts.edu.co/sitio/bienestar-institucional/",
                consultationDate = "19/11/2026",
                publicationDate = "2026",
                vigencia = "Vigente",
                supportedDataType = "Servicios de salud, psicología, torneos deportivos, gimnasio y talleres culturales gratuitos",
                legalSummary = "Espacios de formación integral y permanencia estudiantil de acceso libre para estudiantes activos."
            )
        )
    }
}
