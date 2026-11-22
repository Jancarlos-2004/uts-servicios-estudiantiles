package co.edu.uts.serviciosestudiantiles

import android.app.Application
import co.edu.uts.serviciosestudiantiles.data.repository.AppContainer
import co.edu.uts.serviciosestudiantiles.data.repository.DefaultAppContainer

/**
 * Clase Application personalizada para inicializar el contenedor de dependencias.
 */
class UtsApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}

