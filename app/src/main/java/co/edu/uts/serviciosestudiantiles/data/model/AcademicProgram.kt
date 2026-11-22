package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Representa un Programa Académico oficial de las Unidades Tecnológicas de Santander (UTS).
 * Incluye trazabilidad al Sistema Nacional de Información de la Educación Superior (SNIES)
 * y fuentes institucionales del portal oficial www.uts.edu.co.
 *
 * Fuente: Portal Oficial UTS - Oferta Académica y Registros Calificados MEN / SNIES.
 * Fecha de consulta: Noviembre 2026.
 */
data class AcademicProgram(
    val id: String,
    val name: String,
    val faculty: Faculty,
    val level: AcademicLevel,
    val modalities: List<Modality>,
    val campuses: List<Campus>,
    val sniesCode: String,
    val durationSemesters: Int,
    val degreeTitle: String,
    val isPropedeuticCycle: Boolean = true,
    val officialSourceUrl: String = "https://www.uts.edu.co/sitio/oferta-academica-2/"
) {
    companion object {
        /**
         * Catálogo Oficial Completo y Verificado de Programas de las UTS.
         */
        val OFFICIAL_PROGRAMS: List<AcademicProgram> = listOf(
            // =========================================================================
            // FACULTAD DE CIENCIAS NATURALES E INGENIERÍAS
            // =========================================================================
            AcademicProgram(
                id = "FCENI-T-SIS",
                name = "Tecnología en Desarrollo de Sistemas Informáticos",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL, Modality.VIRTUAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.BARRANCABERMEJA, Campus.VELEZ),
                sniesCode = "53488",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Desarrollo de Sistemas Informáticos"
            ),
            AcademicProgram(
                id = "FCENI-U-SIS",
                name = "Ingeniería de Sistemas",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL, Modality.VIRTUAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.BARRANCABERMEJA),
                sniesCode = "102213",
                durationSemesters = 4,
                degreeTitle = "Ingeniero(a) de Sistemas"
            ),
            AcademicProgram(
                id = "FCENI-T-AMB",
                name = "Tecnología en Manejo de Recursos Ambientales",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.VELEZ, Campus.PIEDECUESTA),
                sniesCode = "53492",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Manejo de Recursos Ambientales"
            ),
            AcademicProgram(
                id = "FCENI-U-AMB",
                name = "Ingeniería Ambiental",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102212",
                durationSemesters = 4,
                degreeTitle = "Ingeniero(a) Ambiental"
            ),
            AcademicProgram(
                id = "FCENI-T-ELE",
                name = "Tecnología en Electricidad Industrial",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.BARRANCABERMEJA),
                sniesCode = "53486",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Electricidad Industrial"
            ),
            AcademicProgram(
                id = "FCENI-U-ELE",
                name = "Ingeniería Eléctrica",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102214",
                durationSemesters = 4,
                degreeTitle = "Ingeniero(a) Electricista"
            ),
            AcademicProgram(
                id = "FCENI-T-ELC",
                name = "Tecnología en Electrónica Industrial",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "53487",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Electrónica Industrial"
            ),
            AcademicProgram(
                id = "FCENI-U-ELC",
                name = "Ingeniería Electrónica",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102215",
                durationSemesters = 4,
                degreeTitle = "Ingeniero(a) Electrónico(a)"
            ),
            AcademicProgram(
                id = "FCENI-T-MEC",
                name = "Tecnología en Operación y Mantenimiento Electromecánico",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.BARRANCABERMEJA),
                sniesCode = "53489",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Operación y Mantenimiento Electromecánico"
            ),
            AcademicProgram(
                id = "FCENI-U-MEC",
                name = "Ingeniería Electromecánica",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.BARRANCABERMEJA),
                sniesCode = "102216",
                durationSemesters = 4,
                degreeTitle = "Ingeniero(a) Electromecánico(a)"
            ),
            AcademicProgram(
                id = "FCENI-T-IND",
                name = "Tecnología en Producción Industrial",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "53490",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Producción Industrial"
            ),
            AcademicProgram(
                id = "FCENI-U-IND",
                name = "Ingeniería Industrial",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102217",
                durationSemesters = 4,
                degreeTitle = "Ingeniero(a) Industrial"
            ),
            AcademicProgram(
                id = "FCENI-T-TOP",
                name = "Tecnología en Topografía",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "53491",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Topografía"
            ),
            AcademicProgram(
                id = "FCENI-T-TEL",
                name = "Tecnología en Gestión de Sistemas de Telecomunicaciones",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "53493",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Gestión de Sistemas de Telecomunicaciones"
            ),
            AcademicProgram(
                id = "FCENI-U-TEL",
                name = "Ingeniería de Telecomunicaciones",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102218",
                durationSemesters = 4,
                degreeTitle = "Ingeniero(a) de Telecomunicaciones"
            ),
            AcademicProgram(
                id = "FCENI-T-GEO",
                name = "Tecnología en Estudios Geotécnicos",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "101859",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Estudios Geotécnicos"
            ),
            AcademicProgram(
                id = "FCENI-T-PET",
                name = "Tecnología en Manejo de Petróleo y Gas en Superficie",
                faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BARRANCABERMEJA),
                sniesCode = "104169",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Manejo de Petróleo y Gas en Superficie"
            ),

            // =========================================================================
            // FACULTAD DE CIENCIAS SOCIOECONÓMICAS Y EMPRESARIALES
            // =========================================================================
            AcademicProgram(
                id = "FCSE-T-GEM",
                name = "Tecnología en Gestión Empresarial",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL, Modality.VIRTUAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.PIEDECUESTA, Campus.VELEZ, Campus.BARRANCABERMEJA),
                sniesCode = "53494",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Gestión Empresarial"
            ),
            AcademicProgram(
                id = "FCSE-U-ADM",
                name = "Administración de Empresas",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL, Modality.VIRTUAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.PIEDECUESTA, Campus.VELEZ, Campus.BARRANCABERMEJA),
                sniesCode = "102219",
                durationSemesters = 4,
                degreeTitle = "Administrador(a) de Empresas"
            ),
            AcademicProgram(
                id = "FCSE-T-CON",
                name = "Tecnología en el Manejo de la Información Contable",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.VELEZ, Campus.BARRANCABERMEJA),
                sniesCode = "53495",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en el Manejo de la Información Contable"
            ),
            AcademicProgram(
                id = "FCSE-U-CON",
                name = "Contaduría Pública",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA, Campus.VELEZ, Campus.BARRANCABERMEJA),
                sniesCode = "102220",
                durationSemesters = 4,
                degreeTitle = "Contador(a) Público(a)"
            ),
            AcademicProgram(
                id = "FCSE-T-MER",
                name = "Tecnología en Mercadeo y Gestión Comercial",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "53496",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Mercadeo y Gestión Comercial"
            ),
            AcademicProgram(
                id = "FCSE-U-MER",
                name = "Mercadeo",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102221",
                durationSemesters = 4,
                degreeTitle = "Profesional en Mercadeo"
            ),
            AcademicProgram(
                id = "FCSE-T-BAN",
                name = "Tecnología en Gestión Bancaria y Financiera",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "53497",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Gestión Bancaria y Financiera"
            ),
            AcademicProgram(
                id = "FCSE-U-FIN",
                name = "Administración Financiera",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102222",
                durationSemesters = 4,
                degreeTitle = "Administrador(a) Financiero(a)"
            ),
            AcademicProgram(
                id = "FCSE-T-MOD",
                name = "Tecnología en Gestión de la Moda",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.TECNOLOGICO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102431",
                durationSemesters = 6,
                degreeTitle = "Tecnólogo(a) en Gestión de la Moda"
            ),
            AcademicProgram(
                id = "FCSE-U-MOD",
                name = "Profesional en Diseño de Moda",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "102432",
                durationSemesters = 4,
                degreeTitle = "Diseñador(a) de Moda"
            ),
            AcademicProgram(
                id = "FCSE-U-DEP",
                name = "Profesional en Cultura Física y Deporte",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "106093",
                durationSemesters = 8,
                degreeTitle = "Profesional en Cultura Física y Deporte"
            ),
            AcademicProgram(
                id = "FCSE-U-NEG",
                name = "Profesional en Negocios Internacionales",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.VIRTUAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "109287",
                durationSemesters = 8,
                degreeTitle = "Profesional en Negocios Internacionales"
            ),
            AcademicProgram(
                id = "FCSE-U-ECO",
                name = "Economía",
                faculty = Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                level = AcademicLevel.UNIVERSITARIO,
                modalities = listOf(Modality.PRESENCIAL),
                campuses = listOf(Campus.BUCARAMANGA),
                sniesCode = "111075",
                durationSemesters = 8,
                degreeTitle = "Economista"
            )
        )

        fun findByFaculty(faculty: Faculty): List<AcademicProgram> {
            return OFFICIAL_PROGRAMS.filter { it.faculty == faculty }
        }

        fun findByName(name: String): AcademicProgram? {
            return OFFICIAL_PROGRAMS.firstOrNull { it.name.equals(name.trim(), ignoreCase = true) }
        }
    }
}
