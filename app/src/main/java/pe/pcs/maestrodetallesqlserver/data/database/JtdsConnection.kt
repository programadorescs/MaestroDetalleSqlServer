package pe.pcs.maestrodetallesqlserver.data.database

import pe.pcs.maestrodetallesqlserver.core.PreferencesKey
import pe.pcs.maestrodetallesqlserver.core.PreferencesProvider
import pe.pcs.maestrodetallesqlserver.core.UtilsSecurity
import java.sql.Connection
import java.sql.DriverManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JtdsConnection @Inject constructor() {

    //***********************************************************//
    // NOTA: Debe asignar la ip del servidor y puerto del mismo  //
    // asi como tambien el nombre de usuario (debe tener permiso //
    // de escritura y lectura sobre la base de datos - dbDatos)  //
    // y clave                                                   //
    // El puerto usado por sql Server debe estar permitido en el //
    // firewall                                                  //
    //***********************************************************//

    fun getConnection(): Connection {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            //val url = "jdbc:jtds:sqlserver://192.168.18.4:1433/dbDatos"
            //return DriverManager.getConnection(url, "Tu_Usuario", "Clave_Usuario")

            return DriverManager.getConnection(
                UtilsSecurity.descifrarDato(
                    PreferencesProvider.getPreferencia(
                        PreferencesKey.CONFIGURAR_SERVER
                    ) ?: ""
                )
            )
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

}