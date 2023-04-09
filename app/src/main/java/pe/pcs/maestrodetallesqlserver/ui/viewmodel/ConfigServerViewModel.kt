package pe.pcs.maestrodetallesqlserver.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.pcs.maestrodetallesqlserver.core.PreferencesKey
import pe.pcs.maestrodetallesqlserver.core.PreferencesProvider
import pe.pcs.maestrodetallesqlserver.core.UtilsSecurity

class ConfigServerViewModel: ViewModel() {

    private val _msgError = MutableLiveData<String>()
    val msgError: LiveData<String> = _msgError

    private val _msgExito = MutableLiveData<String>()
    val msgExito: LiveData<String> = _msgExito

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    fun limpiarError() {
        _msgError.postValue("")
    }

    fun limpiarExito() {
        _msgExito.postValue("")
    }

    fun verificarConfiguracion() {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    PreferencesProvider.getPreferencia(PreferencesKey.CONFIGURAR_SERVER)
                } catch (e: Exception) {
                    _msgError.postValue(e.message)
                    ""
                } finally {
                    _progressBar.postValue(false)
                }
            }

            if(result.isNullOrEmpty())
                _msgError.postValue("El servidor NO esta configurado...")
            else
                _msgExito.postValue("Configurado")
        }
    }

    fun registrarConfiguracion(cadena: String) {
        _progressBar.postValue(true)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    PreferencesProvider.setConfigurarServer(
                        UtilsSecurity.cifrarDato(cadena)
                    )
                    "La configuración del servidor fue exitoso, ya puede ingresar a la app."
                } catch (e: Exception) {
                    _msgError.postValue(e.message)
                    ""
                } finally {
                    _progressBar.postValue(false)
                }
            }

            _msgExito.postValue(result)
        }
    }
}