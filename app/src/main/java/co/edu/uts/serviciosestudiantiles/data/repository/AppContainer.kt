package co.edu.uts.serviciosestudiantiles.data.repository

import android.content.Context
import co.edu.uts.serviciosestudiantiles.data.local.UtsDatabase

/**
 * Contenedor de dependencias de la aplicación (Manual Dependency Injection).
 * Provee instancias únicas de la base de datos y los repositorios institucionales.
 */
interface AppContainer {
    val profileRepository: ProfileRepository
    val tuitionRepository: TuitionRepository
    val institutionalRepository: InstitutionalRepository
    val studentRegistryRepository: StudentRegistryRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: UtsDatabase by lazy {
        UtsDatabase.getDatabase(context)
    }

    override val profileRepository: ProfileRepository by lazy {
        RoomProfileRepository(database.studentProfileDao())
    }

    override val tuitionRepository: TuitionRepository by lazy {
        RoomTuitionRepository(database.tuitionLiquidationDao())
    }

    override val institutionalRepository: InstitutionalRepository by lazy {
        DefaultInstitutionalRepository()
    }

    override val studentRegistryRepository: StudentRegistryRepository by lazy {
        RoomStudentRegistryRepository(database.registeredStudentDao())
    }
}
