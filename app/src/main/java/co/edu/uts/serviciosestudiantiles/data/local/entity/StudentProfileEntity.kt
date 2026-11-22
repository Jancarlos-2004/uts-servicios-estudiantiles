package co.edu.uts.serviciosestudiantiles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile

/**
 * Entidad Room para la persistencia del perfil académico del estudiante en la base de datos local.
 * Vincula el registro con la estructura oficial de programas, sedes y modalidades.
 */
@Entity(tableName = "student_profile")
data class StudentProfileEntity(
    @PrimaryKey
    val id: Long = 1L,
    val fullName: String,
    val studentCode: String,
    val documentNumber: String = "",
    val faculty: String,
    val institutionalEmail: String,
    val programName: String = "",
    val academicLevel: String = "",
    val campus: String = "",
    val modality: String = "",
    val registeredAt: Long
) {
    fun toDomain(): StudentProfile {
        return StudentProfile(
            id = id,
            fullName = fullName,
            studentCode = studentCode,
            documentNumber = documentNumber,
            faculty = Faculty.fromDisplayName(faculty) ?: Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
            institutionalEmail = institutionalEmail,
            programName = programName,
            academicLevel = academicLevel,
            campus = campus,
            modality = modality,
            registeredAt = registeredAt
        )
    }

    companion object {
        fun fromDomain(profile: StudentProfile): StudentProfileEntity {
            return StudentProfileEntity(
                id = profile.id,
                fullName = profile.fullName,
                studentCode = profile.studentCode,
                documentNumber = profile.documentNumber,
                faculty = profile.faculty.displayName,
                institutionalEmail = profile.institutionalEmail,
                programName = profile.programName,
                academicLevel = profile.academicLevel,
                campus = profile.campus,
                modality = profile.modality,
                registeredAt = profile.registeredAt
            )
        }
    }
}
