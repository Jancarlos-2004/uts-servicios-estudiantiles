package co.edu.uts.serviciosestudiantiles.data.repository

import co.edu.uts.serviciosestudiantiles.data.local.dao.StudentProfileDao
import co.edu.uts.serviciosestudiantiles.data.local.entity.StudentProfileEntity
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Interfaz del Repositorio de Perfil Estudiantil.
 * Abstrae el origen de datos (Room / Local) para los ViewModels.
 */
interface ProfileRepository {
    fun getProfileFlow(): Flow<StudentProfile?>
    suspend fun getProfile(): StudentProfile?
    suspend fun saveProfile(profile: StudentProfile)
    suspend fun clearProfile()
}

/**
 * Implementación de ProfileRepository conectada a Room Database.
 */
class RoomProfileRepository(
    private val dao: StudentProfileDao
) : ProfileRepository {

    override fun getProfileFlow(): Flow<StudentProfile?> {
        return dao.getProfileFlow().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getProfile(): StudentProfile? {
        return dao.getProfile()?.toDomain()
    }

    override suspend fun saveProfile(profile: StudentProfile) {
        dao.insertProfile(StudentProfileEntity.fromDomain(profile))
    }

    override suspend fun clearProfile() {
        dao.clearProfile()
    }
}

