package pe.pcs.maestrodetallesqlserver.data.repository

import pe.pcs.maestrodetallesqlserver.core.ResponseStatus
import pe.pcs.maestrodetallesqlserver.data.dao.ProductoDao
import pe.pcs.maestrodetallesqlserver.core.makeCall
import pe.pcs.maestrodetallesqlserver.data.model.ProductoModel
import javax.inject.Inject

class ProductoRepository @Inject constructor(private val dao: ProductoDao) {

    suspend fun listar(dato: String): ResponseStatus<List<ProductoModel>> {
        return makeCall { dao.listar(dato) }
    }

    suspend fun grabar(entidad: ProductoModel): ResponseStatus<Int> {
        return makeCall {
            if (entidad.id == 0) {
                dao.registrar(entidad)
            } else {
                dao.actualizar(entidad)
            }
        }
    }

    suspend fun eliminar(entidad: ProductoModel): ResponseStatus<Int> {
        return makeCall { dao.eliminar(entidad) }
    }

}