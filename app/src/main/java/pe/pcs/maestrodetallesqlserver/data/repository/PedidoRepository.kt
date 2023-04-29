package pe.pcs.maestrodetallesqlserver.data.repository

import pe.pcs.maestrodetallesqlserver.core.ResponseStatus
import pe.pcs.maestrodetallesqlserver.data.dao.PedidoDao
import pe.pcs.maestrodetallesqlserver.data.dao.ProductoDao
import pe.pcs.maestrodetallesqlserver.core.makeCall
import pe.pcs.maestrodetallesqlserver.data.model.DetallePedidoModel
import pe.pcs.maestrodetallesqlserver.data.model.PedidoModel
import pe.pcs.maestrodetallesqlserver.data.model.ProductoModel
import javax.inject.Inject

class PedidoRepository @Inject constructor(
    private val dao: PedidoDao,
    private val producto: ProductoDao
) {

    suspend fun listarPedidoPorFecha(
        desde: String,
        hasta: String
    ): ResponseStatus<List<PedidoModel>> {
        return makeCall { dao.listarPedidoPorFecha(desde, hasta) }
    }

    suspend fun listarDetallePedido(idPedido: Int): ResponseStatus<List<DetallePedidoModel>> {
        return makeCall { dao.listarDetallePedido(idPedido) }
    }

    suspend fun listarProducto(dato: String): ResponseStatus<List<ProductoModel>> {
        return makeCall {
            producto.listar(dato)
        }
    }

    suspend fun registrarPedido(entidad: PedidoModel): ResponseStatus<Int> {
        return makeCall { dao.registrarPedido(entidad) }
    }

    suspend fun anularPedido(idPedido: Int): ResponseStatus<Int> {
        return makeCall { dao.anularPedido(idPedido) }
    }
}