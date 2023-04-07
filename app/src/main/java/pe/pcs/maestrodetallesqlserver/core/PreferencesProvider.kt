package pe.pcs.maestrodetallesqlserver.core

import android.content.Context
import pe.pcs.maestrodetallesqlserver.MaestroDetalleSqlServerApp

object PreferencesProvider {

    fun setConfigurarServer(cadenaConexion: String) {
        val editor = MaestroDetalleSqlServerApp.getAppContext()
            .getSharedPreferences(Constants.PREFS_APP, Context.MODE_PRIVATE).edit()

        editor.putString(
            PreferencesKey.CONFIGURAR_SERVER.name,
            cadenaConexion.trim()
        )
        editor.apply()
    }

    fun getPreferencia(key: PreferencesKey): String? {
        return MaestroDetalleSqlServerApp.getAppContext().getSharedPreferences(
            Constants.PREFS_APP,
            Context.MODE_PRIVATE
        ).getString(key.name, "")
    }

}