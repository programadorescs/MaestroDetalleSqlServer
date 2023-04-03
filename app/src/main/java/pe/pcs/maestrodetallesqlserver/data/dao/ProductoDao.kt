package pe.pcs.maestrodetallesqlserver.data.dao

import pe.pcs.maestrodetallesqlserver.data.database.JtdsConnection
import pe.pcs.maestrodetallesqlserver.data.model.ProductoModel
import javax.inject.Inject

class ProductoDao @Inject constructor(private val jtdsConnection: JtdsConnection) {

    fun listar(dato: String): List<ProductoModel> {

        val lista = mutableListOf<ProductoModel>()

        try {
            val ps = jtdsConnection.getConnection().prepareStatement(
                "SELECT id, descripcion, precio FROM Producto WHERE Descripcion LIKE '%' + ? + '%';"
            )

            ps.setString(1, dato)

            val rs = ps.executeQuery()

            while (rs.next()) {
                lista.add(
                    ProductoModel().apply {
                        id = rs.getInt("id")
                        descripcion = rs.getString("descripcion")
                        precio = rs.getDouble("precio")
                    }
                )
            }

            ps.close()

            return lista
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun registrar(entidad: ProductoModel): Int {
        try {
            val verificar = "SELECT isnull(Count(id), 0) FROM Producto WHERE descripcion=?;"
            val insertar = "INSERT INTO Producto (descripcion, precio) VALUES (?, ?);"

            var ps = jtdsConnection.getConnection().prepareStatement(verificar)
            ps.setString(1, entidad.descripcion)

            val rs = ps.executeQuery()

            if (rs.next() && rs.getInt(1) > 0) {
                rs.close()
                throw Exception("La descripcion ya existe en la DB.")
            }

            ps = jtdsConnection.getConnection().prepareStatement(insertar)
            ps.clearParameters()
            ps.setString(1, entidad.descripcion)
            ps.setDouble(2, entidad.precio)

            return if (ps.executeUpdate() > 0) 1
            else
                throw Exception("Error desconocido, no se puedo realizar la operación")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun actualizar(entidad: ProductoModel): Int {
        try {
            val verificar =
                "SELECT isnull(Count(id), 0) FROM Producto WHERE id<>? AND descripcion=?;"
            val actualizar = "UPDATE Producto SET descripcion=?, precio=? WHERE id= ?;"

            var ps = jtdsConnection.getConnection().prepareStatement(verificar)
            ps.setInt(1, entidad.id)
            ps.setString(2, entidad.descripcion)

            val rs = ps.executeQuery()

            if (rs.next() && rs.getInt(1) > 0) {
                rs.close()
                throw Exception("El producto ya existe en la DB.")
            }

            ps = jtdsConnection.getConnection().prepareStatement(actualizar)
            ps.clearParameters()
            ps.setString(1, entidad.descripcion)
            ps.setDouble(2, entidad.precio)
            ps.setInt(3, entidad.id)

            return if (ps.executeUpdate() > 0) 1
            else
                throw Exception("Error desconocido, no se puedo realizar la operación")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun eliminar(entidad: ProductoModel): Int {
        try {
            val verificar = "SELECT isnull(Count(id), 0) FROM DetallePedido WHERE idproducto=?;"
            val eliminar = "DELETE FROM Producto WHERE id= ?;"

            var ps = jtdsConnection.getConnection().prepareStatement(verificar)
            ps.setInt(1, entidad.id)

            val rs = ps.executeQuery()

            if (rs.next() && rs.getInt(1) > 0) {
                rs.close()
                throw Exception("El producto esta relacionado a otras entidades, imposible eliminar.")
            }

            ps = jtdsConnection.getConnection().prepareStatement(eliminar)
            ps.clearParameters()
            ps.setInt(1, entidad.id)

            return if (ps.executeUpdate() > 0) 1
            else
                throw Exception("Error desconocido, no se puedo realizar la operación")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}