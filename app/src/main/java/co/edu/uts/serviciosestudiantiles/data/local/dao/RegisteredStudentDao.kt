package co.edu.uts.serviciosestudiantiles.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.edu.uts.serviciosestudiantiles.data.local.entity.RegisteredStudentEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para la gestión integral de Estudiantes Registrados.
 */
@Dao
interface RegisteredStudentDao {

    @Query("SELECT * FROM registered_students ORDER BY registeredAt DESC")
    fun getAllStudentsFlow(): Flow<List<RegisteredStudentEntity>>

    @Query("SELECT * FROM registered_students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Long): RegisteredStudentEntity?

    @Query("SELECT * FROM registered_students WHERE studentCode = :code LIMIT 1")
    suspend fun getStudentByCode(code: String): RegisteredStudentEntity?

    @Query("SELECT * FROM registered_students WHERE documentNumber = :doc LIMIT 1")
    suspend fun getStudentByDocument(doc: String): RegisteredStudentEntity?

    @Query("SELECT COUNT(*) FROM registered_students")
    fun getStudentCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStudent(student: RegisteredStudentEntity): Long

    @Update
    suspend fun updateStudent(student: RegisteredStudentEntity)

    @Delete
    suspend fun deleteStudent(student: RegisteredStudentEntity)

    @Query("DELETE FROM registered_students WHERE id = :id")
    suspend fun deleteStudentById(id: Long)
}
