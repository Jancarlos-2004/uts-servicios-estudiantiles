package co.edu.uts.serviciosestudiantiles.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.RegisteredStudent

/**
 * Entidad Room para la persistencia general de estudiantes en la base de datos local.
 * Incluye índices únicos para código estudiantil y documento de identidad para evitar duplicados.
 */
@Entity(
    tableName = "registered_students",
    indices = [
        Index(value = ["studentCode"], unique = true),
        Index(value = ["documentNumber"], unique = true)
    ]
)
data class RegisteredStudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val fullName: String,
    val studentCode: String,
    val documentNumber: String,
    val faculty: String,
    val programName: String,
    val academicLevel: String,
    val campus: String,
    val modality: String,
    val institutionalEmail: String,
    val status: String,
    val registeredAt: Long
) {
    fun toDomain(): RegisteredStudent {
        return RegisteredStudent(
            id = id,
            fullName = fullName,
            studentCode = studentCode,
            documentNumber = documentNumber,
            faculty = Faculty.fromDisplayName(faculty) ?: Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
            programName = programName,
            academicLevel = academicLevel,
            campus = campus,
            modality = modality,
            institutionalEmail = institutionalEmail,
            status = status,
            registeredAt = registeredAt
        )
    }

    companion object {
        fun fromDomain(student: RegisteredStudent): RegisteredStudentEntity {
            return RegisteredStudentEntity(
                id = student.id,
                fullName = student.fullName,
                studentCode = student.studentCode,
                documentNumber = student.documentNumber,
                faculty = student.faculty.displayName,
                programName = student.programName,
                academicLevel = student.academicLevel,
                campus = student.campus,
                modality = student.modality,
                institutionalEmail = student.institutionalEmail,
                status = student.status,
                registeredAt = student.registeredAt
            )
        }
    }
}
