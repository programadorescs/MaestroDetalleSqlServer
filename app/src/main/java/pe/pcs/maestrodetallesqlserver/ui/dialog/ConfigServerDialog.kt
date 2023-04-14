package pe.pcs.maestrodetallesqlserver.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pe.pcs.maestrodetallesqlserver.core.UtilsMessage
import pe.pcs.maestrodetallesqlserver.databinding.DialogoConfigServerBinding
import pe.pcs.maestrodetallesqlserver.ui.viewmodel.ConfigServerViewModel

class ConfigServerDialog : DialogFragment() {

    private lateinit var binding: DialogoConfigServerBinding
    private val viewModel: ConfigServerViewModel by viewModels()
    private var titulo: String = ""

    companion object {
        fun newInstance(mTitulo: String): ConfigServerDialog {
            val instancia = ConfigServerDialog()
            instancia.isCancelable = false
            instancia.titulo = mTitulo

            return instancia
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogoConfigServerBinding.inflate(onGetLayoutInflater(savedInstanceState))

        viewModel.progressBar.observe(this) {
            binding.progressBar.isVisible = it
        }

        viewModel.msgError.observe(this) {
            if (it.isEmpty()) return@observe

            UtilsMessage.showAlertOk("ERROR", it, requireContext())
            viewModel.limpiarError()
        }

        viewModel.msgExito.observe(this) {
            if (it.isEmpty()) return@observe

            UtilsMessage.showAlertOk("FELICITACIONES", it, requireContext())
            viewModel.limpiarExito()
        }

        return MaterialAlertDialogBuilder(requireContext()).apply {
            setView(binding.root)
            setCancelable(false)
            setTitle(titulo)

            setPositiveButton("Aceptar") { dialog, _ ->
                if (binding.etIp.text.toString().isEmpty() ||
                    binding.etPuerto.text.toString().isEmpty() ||
                    binding.etUsuario.text.toString().isEmpty() ||
                    binding.etClave.text.toString().isEmpty()
                ) {
                    UtilsMessage.showToast("Todos los datos son obligatorios")
                    return@setPositiveButton
                }

                viewModel.registrarConfiguracion(
                    "jdbc:jtds:sqlserver://" + binding.etIp.text.toString().trim() +
                            ":" + binding.etPuerto.text.toString().trim() +
                            ";databaseName=dbDatos" +
                            ";user=" + binding.etUsuario.text.toString().trim() +
                            ";password=" + binding.etClave.text.toString().trim() + ";"
                )

                dialog.dismiss()
            }

            setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        }.create()
    }

}