package pe.pcs.maestrodetallesqlserver.data.dao

import pe.pcs.maestrodetallesqlserver.data.database.JtdsConnection
import pe.pcs.maestrodetallesqlserver.data.model.DetallePedidoModel
import pe.pcs.maestrodetallesqlserver.data.model.PedidoModel
import pe.pcs.maestrodetallesqlserver.data.model.ProductoModel
import java.sql.PreparedStatement
import javax.inject.Inject

class PedidoDao @Inject constructor(private val jtdsConnection: JtdsConnection) {

    fun listarPedidoPorFecha(desde: String, hasta: String): List<PedidoModel> {

        val lista = mutableListOf<PedidoModel>()

        try {
            val ps = jtdsConnection.getConnection().prepareStatement(
                "SELECT id, fecha, cliente, estado, total FROM Pedido WHERE Convert(DATE, Fecha) >= ? AND Convert(DATE, Fecha) <= ? ORDER BY id DESC;"
            )

            ps.setString(1, desde)
            ps.setString(2, hasta)

            val rs = ps.executeQuery()

            while (rs.next()) {
                lista.add(
                    PedidoModel().apply {
                        id = rs.getInt("id")
                        fecha = rs.getDate("fecha")
                        cliente = rs.getString("cliente")
                        estado = rs.getString("estado")
                        total = rs.getDouble("total")
                    }
                )
            }

            ps.close()

            return lista
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun listarDetallePedido(idPedido: Int): List<DetallePedidoModel> {

        val lista = mutableListOf<DetallePedidoModel>()

        try {
            val ps = jtdsConnection.getConnection().prepareStatement(
                "SELECT descripcion, cantidad, detallePedido.precio, importe FROM Producto INNER JOIN detallePedido ON producto.id = detallePedido.idproducto WHERE idPedido = ?;"
            )

            ps.setInt(1, idPedido)

            val rs = ps.executeQuery()

            while (rs.next()) {
                lista.add(
                    DetallePedidoModel().apply {
                        cantidad = rs.getInt("cantidad")
                        precio = rs.getDouble("precio")
                        importe = rs.getDouble("importe")
                        mProducto =
                            ProductoModel().apply { descripcion = rs.getString("descripcion") }
                    }
                )
            }

            ps.close()

            return lista
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun registrarPedido(entidad: PedidoModel): Int {
        val cn = jtdsConnection.getConnection()

        try {
            if(entidad.detalles.isEmpty())
                throw Exception("El pedido no cuenta con items en su detalle.")

            val insertarPedido =
                "INSERT INTO Pedido(fecha, cliente, estado, total) VALUES(?, ?, ?, ?);"
            val insertarDetalle =
                "INSERT INTO DetallePedido (idpedido, idproducto, cantidad, precio, importe) VALUES (?, ?, ?, ?, ?);"

            cn.autoCommit = false

            var ps = cn.prepareStatement(insertarPedido, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setDate(1, entidad.fecha)
            ps.setString(2, entidad.cliente)
            ps.setString(3, entidad.estado)
            ps.setDouble(4, entidad.total)

            ps.executeUpdate()
            val rs = ps.generatedKeys

            var keyPedido = 0
            if (rs.next()) keyPedido = rs.getInt(1)
            rs.close()

            ps = cn.prepareStatement(insertarDetalle)
            ps.clearParameters()

            for (det in entidad.detalles) {
                ps.setInt(1, keyPedido)
                ps.setInt(2, det.mProducto?.id ?: 0)
                ps.setInt(3, det.cantidad)
                ps.setDouble(4, det.precio)
                ps.setDouble(5, det.importe)

                ps.executeUpdate()
                ps.clearParameters()
            }

            cn.commit()
            cn.autoCommit = true

            return if (keyPedido > 0) keyPedido
            else
                throw Exception("Error desconocido, NO se concreto la operación")
        } catch (e: Exception) {
            try {
                cn.rollback()
            } catch (ex: Exception) {
                throw Exception(ex.message)
            }

            throw Exception(e.message)
        }
    }

    fun anularPedido(idPedido: Int): Int {
        try {
            val verificar = "SELECT isnull(Count(id), 0) FROM Pedido WHERE estado='Anulado' AND id=?;"
            val anular = "UPDATE Pedido Set estado='Anulado' WHERE id= ?;"

            var ps = jtdsConnection.getConnection().prepareStatement(verificar)
            ps.setInt(1, idPedido)

            val rs = ps.executeQuery()
            ps.clearParameters()

            if (rs.next() && rs.getInt(1) > 0) {
                rs.close()
                throw Exception("El pedido ya se encuentra anulado, imposible anular.")
            }

            ps = jtdsConnection.getConnection().prepareStatement(anular)
            ps.setInt(1, idPedido)

            return if (ps.executeUpdate() > 0) 1
            else
                throw Exception("Error desconocido, no se puedo realizar la operación")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

}