package pe.pcs.maestrodetallesqlserver.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.pcs.maestrodetallesqlserver.core.ResponseStatus
import pe.pcs.maestrodetallesqlserver.data.model.DetallePedidoModel
import pe.pcs.maestrodetallesqlserver.data.model.PedidoModel
import pe.pcs.maestrodetallesqlserver.data.model.ProductoModel
import pe.pcs.maestrodetallesqlserver.data.repository.PedidoRepository
import javax.inject.Inject

@HiltViewModel
class PedidoViewModel @Inject constructor(
    private val repository : PedidoRepository
): ViewModel() {

    var listaProducto = MutableLiveData<List<ProductoModel>?>()

    private var _listaCarrito = MutableLiveData<MutableList<DetallePedidoModel>>()
    var listaCarrito: MutableLiveData<MutableList<DetallePedidoModel>> = _listaCarrito

    private var _totalItem = MutableLiveData<Int>()
    var totalItem: LiveData<Int> = _totalItem

    private var _totalImporte = MutableLiveData<Double>()
    var totalImporte: LiveData<Double> = _totalImporte

    private var _itemProducto = MutableLiveData<ProductoModel?>()
    val itemProducto: LiveData<ProductoModel?> = _itemProducto

    private val _status = MutableLiveData<ResponseStatus<List<ProductoModel>>>()
    val status: LiveData<ResponseStatus<List<ProductoModel>>> = _status

    private val _statusInt = MutableLiveData<ResponseStatus<Int>>()
    val statusInt: LiveData<ResponseStatus<Int>> = _statusInt

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    init {
        _listaCarrito.value = mutableListOf()
    }

    fun setLimpiarMensaje() {
        _mensaje.postValue("")
    }

    // Para el item seleccionado
    fun setItemProducto(item: ProductoModel?) {
        _itemProducto.value = item
    }

    fun agregarProductoCarrito(cantidad: Int, precio: Double) {
        if (cantidad == 0 || precio == 0.0) return

        if (itemProducto.value == null) return

        for (det in listaCarrito.value!!) {
            if (det.mProducto?.id == itemProducto.value?.id) {
                setItemProducto(null)
                _mensaje.postValue("Ya existe este elemento en su lista...")
                return
            }
        }

        val entidad = DetallePedidoModel().apply {
            this.mProducto = itemProducto.value
            this.cantidad = cantidad
            this.precio = precio
            this.importe = cantidad * precio
        }

        setItemProducto(null)

        // Agrega el producto a la lista
        _listaCarrito.value?.add(entidad)

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )
    }

    fun quitarProductoCarrito(item: DetallePedidoModel) {
        _listaCarrito.value?.remove(item)

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )

        listaCarrito.postValue(_listaCarrito.value)
    }

    fun limpiarCarrito() {
        _listaCarrito.value = mutableListOf()

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )
    }

    fun listarCarrito() {
        listaCarrito.postValue(_listaCarrito.value)
    }

    fun setAumentarCantidadProducto(item: DetallePedidoModel) {
        _listaCarrito.value?.forEach {
            if(it.mProducto?.id == item.mProducto?.id) {
                it.cantidad++
                it.importe = it.cantidad * it.precio
            }
        }

        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )

        listaCarrito.postValue(_listaCarrito.value)
    }

    fun setDisminuirCantidadProducto(item: DetallePedidoModel) {
        // Recorre la lista para disminuir la cantidad del producto seleccionado
        _listaCarrito.value?.forEach {
            if(it.mProducto?.id == item.mProducto?.id && it.cantidad > 1) {
                it.cantidad--
                it.importe = it.cantidad * it.precio
            }
        }

        // Obtiene la cantidad de producto en el carrito
        _totalItem.postValue(
            listaCarrito.value?.sumOf { it.cantidad }
        )

        // Suma el importe de los productos para obtener el total a pagar
        _totalImporte.postValue(
            listaCarrito.value?.sumOf { it.cantidad * it.precio }
        )

        listaCarrito.postValue(_listaCarrito.value)
    }

    private fun handleResponseStatus(responseStatus: ResponseStatus<List<ProductoModel>>) {
        if (responseStatus is ResponseStatus.Success) {
            listaProducto.value = responseStatus.data
        }

        _status.value = responseStatus
    }

    private fun handleResponseStatusInt(responseStatus: ResponseStatus<Int>) {
        _statusInt.value = responseStatus
    }

    fun listarProducto(dato: String) {
        viewModelScope.launch {
            _status.value = ResponseStatus.Loading()
            handleResponseStatus(repository.listarProducto(dato))
        }
    }

    fun registrarPedido(pedido: PedidoModel) {
        viewModelScope.launch {
            _statusInt.value = ResponseStatus.Loading()
            handleResponseStatusInt(repository.registrarPedido(pedido))
        }
    }

}