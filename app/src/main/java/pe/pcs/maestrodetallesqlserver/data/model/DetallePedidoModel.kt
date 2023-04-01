package pe.pcs.maestrodetallesqlserver.data.model

data class DetallePedidoModel(
    var id: Int = 0,
    var mPedido: PedidoModel? = null,
    var mProducto: ProductoModel? = null,
    var cantidad: Int = 0,
    var precio: Double = 0.0,
    var importe: Double = 0.0
)
