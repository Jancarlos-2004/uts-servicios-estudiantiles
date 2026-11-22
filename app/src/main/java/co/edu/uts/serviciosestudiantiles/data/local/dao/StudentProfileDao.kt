package co.edu.uts.serviciosestudiantiles.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.edu.uts.serviciosestudiantiles.data.local.entity.StudentProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para la tabla de perfil de estudiante.
 */
@Dao
interface StudentProfileDao {

    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<StudentProfileEntity?>

    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): StudentProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: StudentProfileEntity)

    @Query("DELETE FROM student_profile WHERE id = 1")
    suspend fun clearProfile()
}

