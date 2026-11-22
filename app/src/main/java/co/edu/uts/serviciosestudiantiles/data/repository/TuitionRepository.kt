package co.edu.uts.serviciosestudiantiles.data.repository

import co.edu.uts.serviciosestudiantiles.data.local.dao.TuitionLiquidationDao
import co.edu.uts.serviciosestudiantiles.data.local.entity.TuitionLiquidationEntity
import co.edu.uts.serviciosestudiantiles.data.model.TuitionLiquidation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Interfaz del Repositorio de Liquidaciones de Matrícula Extracurricular.
 */
interface TuitionRepository {
    fun getAllLiquidationsFlow(): Flow<List<TuitionLiquidation>>
    fun getTotalAccumulatedFlow(): Flow<Double>
    suspend fun saveLiquidation(liquidation: TuitionLiquidation): Long
    suspend fun clearHistory()
}

/**
 * Implementación de TuitionRepository conectada a Room Database.
 */
class RoomTuitionRepository(
    private val dao: TuitionLiquidationDao
) : TuitionRepository {

    override fun getAllLiquidationsFlow(): Flow<List<TuitionLiquidation>> {
        return dao.getAllLiquidationsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTotalAccumulatedFlow(): Flow<Double> {
        return dao.getTotalAccumulatedFlow()
    }

    override suspend fun saveLiquidation(liquidation: TuitionLiquidation): Long {
        return dao.insertLiquidation(TuitionLiquidationEntity.fromDomain(liquidation))
    }

    override suspend fun clearHistory() {
        dao.clearAll()
    }
}

