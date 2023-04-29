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
import pe.pcs.maestrodetallesqlserver.data.repository.PedidoRepository
import javax.inject.Inject

@HiltViewModel
class ReportePedidoViewModel @Inject constructor(
    private val repository : PedidoRepository
): ViewModel() {

    private val _listaPedido = MutableLiveData<List<PedidoModel>?>()
    val listaPedido: LiveData<List<PedidoModel>?> = _listaPedido

    private val _listaDetalle = MutableLiveData<List<DetallePedidoModel>?>()
    val listaDetalle: LiveData<List<DetallePedidoModel>?> = _listaDetalle

    private val _itemPedido = MutableLiveData<PedidoModel?>()
    val itemPedido: LiveData<PedidoModel?> = _itemPedido

    private val _statusListaPedido = MutableLiveData<ResponseStatus<List<PedidoModel>>>()
    val statusListaPedido: LiveData<ResponseStatus<List<PedidoModel>>> = _statusListaPedido

    private val _statusListaDetalle = MutableLiveData<ResponseStatus<List<DetallePedidoModel>>>()
    val statusListaDetalle: LiveData<ResponseStatus<List<DetallePedidoModel>>> = _statusListaDetalle

    private val _statusInt = MutableLiveData<ResponseStatus<Int>>()
    val statusInt: LiveData<ResponseStatus<Int>> = _statusInt

    init {
        _listaPedido.value = mutableListOf()
        _listaDetalle.value = mutableListOf()
    }

    fun setItem(entidad: PedidoModel?) {
        _itemPedido.postValue(entidad)
    }

    private fun handleResponseStatusPedido(responseStatus: ResponseStatus<List<PedidoModel>>) {
        if (responseStatus is ResponseStatus.Success) {
            _listaPedido.value = responseStatus.data
        }

        _statusListaPedido.value = responseStatus
    }

    private fun handleResponseStatusDetalle(responseStatus: ResponseStatus<List<DetallePedidoModel>>) {
        if (responseStatus is ResponseStatus.Success) {
            _listaDetalle.value = responseStatus.data
        }

        _statusListaDetalle.value = responseStatus
    }

    private fun handleResponseStatusInt(responseStatus: ResponseStatus<Int>) {
        _statusInt.value = responseStatus
    }

    fun anularPedido(id: Int, desde: String, hasta: String) {
        viewModelScope.launch {
            _statusInt.value = ResponseStatus.Loading()
            handleResponseStatusInt(repository.anularPedido(id))
            handleResponseStatusPedido(repository.listarPedidoPorFecha(desde, hasta))
        }
    }

    fun listarPedido(desde: String, hasta: String) {
        viewModelScope.launch {
            _statusListaPedido.value = ResponseStatus.Loading()
            handleResponseStatusPedido(repository.listarPedidoPorFecha(desde, hasta))
        }
    }

    fun listarDetalle(idPedido: Int) {
        viewModelScope.launch {
            _statusListaDetalle.value = ResponseStatus.Loading()
            handleResponseStatusDetalle(repository.listarDetallePedido(idPedido))
        }
    }
}