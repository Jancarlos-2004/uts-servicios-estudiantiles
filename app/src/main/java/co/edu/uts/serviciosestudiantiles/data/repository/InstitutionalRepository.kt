package co.edu.uts.serviciosestudiantiles.data.repository

import co.edu.uts.serviciosestudiantiles.data.model.AcademicLevel
import co.edu.uts.serviciosestudiantiles.data.model.AcademicProgram
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.ContinuingEducationCourse
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalBenefit
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalSource
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.WellnessActivity
import co.edu.uts.serviciosestudiantiles.data.model.WellnessCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Interfaz del Repositorio de Información Institucional Oficial UTS.
 * Provee acceso a la oferta académica, tarifas por vigencia, estímulos,
 * educación continua, bienestar y trazabilidad de fuentes.
 */
interface InstitutionalRepository {
    fun getAllPrograms(): List<AcademicProgram>
    fun getProgramsByFaculty(faculty: Faculty): List<AcademicProgram>
    fun filterPrograms(
        query: String = "",
        faculty: Faculty? = null,
        level: AcademicLevel? = null,
        modality: Modality? = null,
        campus: Campus? = null
    ): List<AcademicProgram>

    fun getTariffs(vigencia: String): List<InstitutionalTariff>
    fun getBenefits(): List<InstitutionalBenefit>
    fun getContinuingEducationOffer(): List<ContinuingEducationCourse>
    fun getWellnessActivities(category: WellnessCategory? = null): List<WellnessActivity>
    fun getOfficialSources(): List<InstitutionalSource>
}

/**
 * Implementación del repositorio institucional precargado con información verificada
 * de los portales y resoluciones oficiales de las Unidades Tecnológicas de Santander.
 */
class DefaultInstitutionalRepository : InstitutionalRepository {

    override fun getAllPrograms(): List<AcademicProgram> {
        return AcademicProgram.OFFICIAL_PROGRAMS
    }

    override fun getProgramsByFaculty(faculty: Faculty): List<AcademicProgram> {
        return AcademicProgram.findByFaculty(faculty)
    }

    override fun filterPrograms(
        query: String,
        faculty: Faculty?,
        level: AcademicLevel?,
        modality: Modality?,
        campus: Campus?
    ): List<AcademicProgram> {
        return AcademicProgram.OFFICIAL_PROGRAMS.filter { program ->
            val matchesQuery = query.isBlank() ||
                    program.name.contains(query, ignoreCase = true) ||
                    program.sniesCode.contains(query, ignoreCase = true)

            val matchesFaculty = faculty == null || program.faculty == faculty
            val matchesLevel = level == null || program.level == level
            val matchesModality = modality == null || program.modalities.contains(modality)
            val matchesCampus = campus == null || program.campuses.contains(campus)

            matchesQuery && matchesFaculty && matchesLevel && matchesModality && matchesCampus
        }
    }

    override fun getTariffs(vigencia: String): List<InstitutionalTariff> {
        return InstitutionalTariff.getTariffsByVigencia(vigencia)
    }

    override fun getBenefits(): List<InstitutionalBenefit> {
        return InstitutionalBenefit.OFFICIAL_BENEFITS
    }

    override fun getContinuingEducationOffer(): List<ContinuingEducationCourse> {
        return ContinuingEducationCourse.OFFICIAL_CONTINUING_ED_OFFER
    }

    override fun getWellnessActivities(category: WellnessCategory?): List<WellnessActivity> {
        return if (category == null) {
            WellnessActivity.OFFICIAL_ACTIVITIES
        } else {
            WellnessActivity.OFFICIAL_ACTIVITIES.filter { it.category == category }
        }
    }

    override fun getOfficialSources(): List<InstitutionalSource> {
        return InstitutionalSource.OFFICIAL_SOURCES
    }
}
