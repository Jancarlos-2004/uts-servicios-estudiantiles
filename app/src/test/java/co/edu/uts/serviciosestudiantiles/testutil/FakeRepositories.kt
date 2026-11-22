package co.edu.uts.serviciosestudiantiles.testutil

import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.data.model.TuitionLiquidation
import co.edu.uts.serviciosestudiantiles.data.repository.ProfileRepository
import co.edu.uts.serviciosestudiantiles.data.repository.TuitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Falso repositorio de Perfil para pruebas unitarias rápidas e independientes del framework.
 */
class FakeProfileRepository : ProfileRepository {
    private val profileFlow = MutableStateFlow<StudentProfile?>(null)

    override fun getProfileFlow(): Flow<StudentProfile?> = profileFlow.asStateFlow()

    override suspend fun getProfile(): StudentProfile? = profileFlow.value

    override suspend fun saveProfile(profile: StudentProfile) {
        profileFlow.value = profile
    }

    override suspend fun clearProfile() {
        profileFlow.value = null
    }
}

/**
 * Falso repositorio de Liquidaciones para pruebas unitarias de cálculo y acumulados.
 */
class FakeTuitionRepository : TuitionRepository {
    private val liquidationsFlow = MutableStateFlow<List<TuitionLiquidation>>(emptyList())

    override fun getAllLiquidationsFlow(): Flow<List<TuitionLiquidation>> = liquidationsFlow.asStateFlow()

    override fun getTotalAccumulatedFlow(): Flow<Double> =
        liquidationsFlow.map { list -> list.sumOf { it.totalAmount } }

    override suspend fun saveLiquidation(liquidation: TuitionLiquidation): Long {
        val current = liquidationsFlow.value.toMutableList()
        val nextId = (current.maxOfOrNull { it.id } ?: 0L) + 1L
        current.add(0, liquidation.copy(id = nextId))
        liquidationsFlow.value = current
        return nextId
    }

    override suspend fun clearHistory() {
        liquidationsFlow.value = emptyList()
    }
}

