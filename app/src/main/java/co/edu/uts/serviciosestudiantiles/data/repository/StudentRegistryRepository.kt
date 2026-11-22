package co.edu.uts.serviciosestudiantiles.data.repository

import co.edu.uts.serviciosestudiantiles.data.local.dao.RegisteredStudentDao
import co.edu.uts.serviciosestudiantiles.data.local.entity.RegisteredStudentEntity
import co.edu.uts.serviciosestudiantiles.data.model.RegisteredStudent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Interfaz del Repositorio para la gestión integral de Estudiantes Registrados.
 */
interface StudentRegistryRepository {
    fun getAllStudentsFlow(): Flow<List<RegisteredStudent>>
    fun getStudentCountFlow(): Flow<Int>
    suspend fun getStudentById(id: Long): RegisteredStudent?
    suspend fun getStudentByCode(code: String): RegisteredStudent?
    suspend fun getStudentByDocument(doc: String): RegisteredStudent?
    suspend fun registerStudent(student: RegisteredStudent): Result<Long>
    suspend fun updateStudent(student: RegisteredStudent): Result<Unit>
    suspend fun deleteStudent(studentId: Long): Result<Unit>
}

/**
 * Implementación de StudentRegistryRepository conectada a Room Database.
 * Incluye validación de duplicados por código estudiantil y documento de identidad.
 */
class RoomStudentRegistryRepository(
    private val dao: RegisteredStudentDao
) : StudentRegistryRepository {

    override fun getAllStudentsFlow(): Flow<List<RegisteredStudent>> {
        return dao.getAllStudentsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getStudentCountFlow(): Flow<Int> {
        return dao.getStudentCountFlow()
    }

    override suspend fun getStudentById(id: Long): RegisteredStudent? {
        return dao.getStudentById(id)?.toDomain()
    }

    override suspend fun getStudentByCode(code: String): RegisteredStudent? {
        return dao.getStudentByCode(code.trim())?.toDomain()
    }

    override suspend fun getStudentByDocument(doc: String): RegisteredStudent? {
        return dao.getStudentByDocument(doc.trim())?.toDomain()
    }

    override suspend fun registerStudent(student: RegisteredStudent): Result<Long> {
        return try {
            val codeTrimmed = student.studentCode.trim()
            val docTrimmed = student.documentNumber.trim()

            // 1. Validar duplicidad por código estudiantil
            val existingByCode = dao.getStudentByCode(codeTrimmed)
            if (existingByCode != null && existingByCode.id != student.id) {
                return Result.failure(
                    IllegalArgumentException("Ya existe un estudiante registrado con el código $codeTrimmed (${existingByCode.fullName}).")
                )
            }

            // 2. Validar duplicidad por documento de identidad
            if (docTrimmed.isNotBlank()) {
                val existingByDoc = dao.getStudentByDocument(docTrimmed)
                if (existingByDoc != null && existingByDoc.id != student.id) {
                    return Result.failure(
                        IllegalArgumentException("Ya existe un estudiante registrado con el documento $docTrimmed (${existingByDoc.fullName}).")
                    )
                }
            }

            val entity = RegisteredStudentEntity.fromDomain(
                student.copy(
                    studentCode = codeTrimmed,
                    documentNumber = docTrimmed
                )
            )

            if (student.id > 0L) {
                dao.updateStudent(entity)
                Result.success(student.id)
            } else {
                val newId = dao.insertStudent(entity)
                Result.success(newId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStudent(student: RegisteredStudent): Result<Unit> {
        return try {
            val codeTrimmed = student.studentCode.trim()
            val docTrimmed = student.documentNumber.trim()

            val existingByCode = dao.getStudentByCode(codeTrimmed)
            if (existingByCode != null && existingByCode.id != student.id) {
                return Result.failure(
                    IllegalArgumentException("El código $codeTrimmed ya pertenece a otro estudiante (${existingByCode.fullName}).")
                )
            }

            if (docTrimmed.isNotBlank()) {
                val existingByDoc = dao.getStudentByDocument(docTrimmed)
                if (existingByDoc != null && existingByDoc.id != student.id) {
                    return Result.failure(
                        IllegalArgumentException("El documento $docTrimmed ya pertenece a otro estudiante (${existingByDoc.fullName}).")
                    )
                }
            }

            val entity = RegisteredStudentEntity.fromDomain(
                student.copy(
                    studentCode = codeTrimmed,
                    documentNumber = docTrimmed
                )
            )
            dao.updateStudent(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteStudent(studentId: Long): Result<Unit> {
        return try {
            dao.deleteStudentById(studentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
