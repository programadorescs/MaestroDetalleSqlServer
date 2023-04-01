package pe.pcs.maestrodetallesqlserver.data.database

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
            val url = "jdbc:jtds:sqlserver://192.168.18.4:1433/dbDatos"
            return DriverManager.getConnection(url, "Tu_Usuario", "Clave_Usuario")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

}