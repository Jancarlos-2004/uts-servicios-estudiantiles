package co.edu.uts.serviciosestudiantiles.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.edu.uts.serviciosestudiantiles.data.local.entity.TuitionLiquidationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para el historial de liquidaciones de matrícula extracurricular.
 */
@Dao
interface TuitionLiquidationDao {

    @Query("SELECT * FROM tuition_liquidations ORDER BY timestamp DESC")
    fun getAllLiquidationsFlow(): Flow<List<TuitionLiquidationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiquidation(entity: TuitionLiquidationEntity): Long

    @Query("SELECT COALESCE(SUM(totalAmount), 0.0) FROM tuition_liquidations")
    fun getTotalAccumulatedFlow(): Flow<Double>

    @Query("DELETE FROM tuition_liquidations")
    suspend fun clearAll()
}

