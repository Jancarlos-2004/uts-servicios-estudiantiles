package co.edu.uts.serviciosestudiantiles.data.model

/**
 * Servicios y Actividades Oficiales de Bienestar Institucional UTS.
 * Fuente: Departamento de Bienestar Institucional UTS (Campus Bucaramanga, Edificio B).
 * Fecha de consulta: Noviembre 2026.
 */
data class WellnessActivity(
    val id: String,
    val title: String,
    val category: WellnessCategory,
    val description: String,
    val schedule: String,
    val location: String,
    val requirements: String,
    val isFreeForStudents: Boolean = true,
    val contactEmail: String = "bienestar@correo.uts.edu.co"
) {
    companion object {
        val OFFICIAL_ACTIVITIES: List<WellnessActivity> = listOf(
            // =========================================================================
            // DEPORTES Y ACTIVIDAD FÍSICA
            // =========================================================================
            WellnessActivity(
                id = "BI-DEP-GIM",
                title = "Gimnasio Universitario y Acondicionamiento Físico",
                category = WellnessCategory.DEPORTE,
                description = "Espacio dotado con máquinas cardiovasculares, pesas y entrenadores para el acondicionamiento físico de la comunidad uteísta.",
                schedule = "Lunes a Viernes: 6:00 a.m. a 8:00 p.m. / Sábados: 7:00 a.m. a 1:00 p.m.",
                location = "Campus Bucaramanga, Edificio B, Piso 1",
                requirements = "Carnet estudiantil vigente, ropa deportiva adecuada y toalla personal."
            ),
            WellnessActivity(
                id = "BI-DEP-TOR",
                title = "Torneo Deportivo Interfacultades (Fútbol, Futsal, Voleibol)",
                category = WellnessCategory.DEPORTE,
                description = "Competencias deportivas semestrales entre programas de la Facultad de Ciencias Naturales e Ingenierías y la Facultad de Ciencias Socioeconómicas.",
                schedule = "Miércoles y Viernes desde las 4:00 p.m. / Sábados en la mañana",
                location = "Canchas múltiples y Polideportivo Campus Bucaramanga",
                requirements = "Estar matriculado en el semestre lectivo y presentar afiliación a EPS/SISBÉN vigente."
            ),
            WellnessActivity(
                id = "BI-DEP-SEL",
                title = "Selecciones Deportivas de Representación UTS",
                category = WellnessCategory.DEPORTE,
                description = "Convocatorias para deportistas de alto rendimiento en taekwondo, judo, baloncesto, atletismo y tenis de mesa con participación en torneos ASCUN.",
                schedule = "Entrenamientos programados según la disciplina deportiva",
                location = "Instalaciones deportivas UTS y escenarios en convenio",
                requirements = "Superar las pruebas de selección técnica y mantener promedio académico nivelado."
            ),

            // =========================================================================
            // CULTURA Y PROMOCIÓN ARTÍSTICA
            // =========================================================================
            WellnessActivity(
                id = "BI-CUL-DAN",
                title = "Grupo Representativo de Danza Folclórica y Moderna",
                category = WellnessCategory.CULTURA,
                description = "Formación en ritmos tradicionales colombianos, danza contemporánea y participación en festivales culturales universitarios.",
                schedule = "Martes y Jueves: 2:00 p.m. a 5:00 p.m.",
                location = "Salón de Expresión Corporal, Campus Bucaramanga",
                requirements = "Audición artística previa y compromiso de asistencia a ensayos."
            ),
            WellnessActivity(
                id = "BI-CUL-MUS",
                title = "Ensamble Musical y Taller de Instrumentos (Música Andina y Rock)",
                category = WellnessCategory.CULTURA,
                description = "Talleres de formación en guitarra, percusión, vientos y conformación de la banda institucional de las UTS.",
                schedule = "Lunes y Miércoles: 3:00 p.m. a 6:00 p.m.",
                location = "Sala de Música, Edificio A, Campus Bucaramanga",
                requirements = "Gusto por la interpretación musical; no se requiere instrumento propio para talleres iniciales."
            ),
            WellnessActivity(
                id = "BI-CUL-TEA",
                title = "Taller de Teatro y Artes Escénicas",
                category = WellnessCategory.CULTURA,
                description = "Desarrollo de habilidades de comunicación asertiva, expresión corporal, pérdida de pánico escénico y montaje de obras teatrales.",
                schedule = "Viernes: 2:00 p.m. a 6:00 p.m.",
                location = "Auditorio de Bienestar Institucional",
                requirements = "Inscripción en línea en las fechas de convocatoria semestral."
            ),

            // =========================================================================
            // PROMOCIÓN DE LA SALUD Y PREVENCIÓN
            // =========================================================================
            WellnessActivity(
                id = "BI-SAL-MED",
                title = "Atención en Medicina General y Primeros Auxilios",
                category = WellnessCategory.SALUD,
                description = "Consulta médica básica, atención de urgencias menores, enfermería, toma de signos vitales y campañas preventivas de salud física.",
                schedule = "Lunes a Viernes: 7:00 a.m. a 9:00 p.m. (Jornada continua)",
                location = "Unidad Médica, Campus Bucaramanga, Edificio B",
                requirements = "Presentar documento de identidad y carnet estudiantil institucional."
            ),
            WellnessActivity(
                id = "BI-SAL-ODO",
                title = "Odontología Preventiva y Profilaxis Dental",
                category = WellnessCategory.SALUD,
                description = "Diagnóstico oral, aplicación de flúor, profilaxis, detartraje supragingival y educación en higiene bucal para estudiantes.",
                schedule = "Lunes a Jueves: 8:00 a.m. a 12:00 m. y 2:00 p.m. a 6:00 p.m.",
                location = "Consultorio Odontológico UTS",
                requirements = "Agendamiento previo en la ventanilla de salud de Bienestar."
            ),

            // =========================================================================
            // DESARROLLO HUMANO Y ACOMPAÑAMIENTO PSICOSOCIAL
            // =========================================================================
            WellnessActivity(
                id = "BI-PSI-IND",
                title = "Orientación Psicológica Individual y Acompañamiento",
                category = WellnessCategory.ACOMPANAMIENTO,
                description = "Espacio confidencial para manejo de estrés académico, ansiedad, dificultades personales, orientación vocacional y bienestar emocional.",
                schedule = "Lunes a Viernes con cita previa concertada",
                location = "Consultorios de Psicología, Edificio B",
                requirements = "Solicitud voluntaria o remisión docente/coordinación."
            ),
            WellnessActivity(
                id = "BI-PSI-DES",
                title = "Programa de Permanencia Estudiantil y Tutorías de Refuerzo",
                category = WellnessCategory.ACOMPANAMIENTO,
                description = "Acompañamiento pedagógico y monitorías académicas en ciencias básicas (matemáticas, física, programación) para evitar la deserción.",
                schedule = "Horarios flexibles según la asignatura de refuerzo",
                location = "Aulas de Tutoría y salas virtuales",
                requirements = "Estar matriculado en cursos de primer a tercer semestre."
            )
        )
    }
}

enum class WellnessCategory(val displayName: String) {
    DEPORTE("Deportes y Actividad Física"),
    CULTURA("Cultura y Promoción Artística"),
    SALUD("Promoción y Prevención en Salud"),
    ACOMPANAMIENTO("Desarrollo Humano y Acompañamiento");

    companion object {
        fun fromDisplayName(name: String?): WellnessCategory {
            return entries.firstOrNull { it.displayName.equals(name?.trim(), ignoreCase = true) } ?: DEPORTE
        }
    }
}
