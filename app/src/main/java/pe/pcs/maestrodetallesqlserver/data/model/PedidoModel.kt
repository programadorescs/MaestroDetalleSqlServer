package pe.pcs.maestrodetallesqlserver.data.model

import java.sql.Date

data class PedidoModel(
    var id: Int = 0,
    var fecha: Date? = null,
    var cliente: String = "",
    var estado: String = "",
    var total: Double = 0.0,
    var detalles: List<DetallePedidoModel> = mutableListOf()
)
