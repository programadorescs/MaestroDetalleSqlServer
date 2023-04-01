package pe.pcs.maestrodetallesqlserver

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MaestroDetalleSqlServerApp: Application() {

    companion object {
        private var instancia: MaestroDetalleSqlServerApp? = null

        fun getAppContext(): Context {
            return instancia!!.applicationContext
        }
    }

    init {
        instancia = this
    }

}