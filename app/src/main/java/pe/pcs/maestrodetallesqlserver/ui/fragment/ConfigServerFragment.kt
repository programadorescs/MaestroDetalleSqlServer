package pe.pcs.maestrodetallesqlserver.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import pe.pcs.maestrodetallesqlserver.core.UtilsCommon
import pe.pcs.maestrodetallesqlserver.core.UtilsMessage
import pe.pcs.maestrodetallesqlserver.databinding.FragmentConfigServerBinding
import pe.pcs.maestrodetallesqlserver.ui.viewmodel.ConfigServerViewModel


class ConfigServerFragment : Fragment() {

    private lateinit var binding: FragmentConfigServerBinding
    private val viewModel: ConfigServerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfigServerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBar.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.msgError.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe

            UtilsMessage.showAlertOk("ERROR", it, requireContext())
            viewModel.limpiarError()
        }

        viewModel.msgExito.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe

            UtilsMessage.showAlertOk("FELICITACIONES", it, requireContext())
            viewModel.limpiarExito()
        }

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.ocultarTeclado(it)

            if (binding.etIp.text.toString().trim().isEmpty() ||
                binding.etPuerto.text.toString().trim().isEmpty() ||
                binding.etUsuario.text.toString().trim().isEmpty() ||
                binding.etClave.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA",
                    "Todos los datos son necesarios",
                    requireContext()
                )
                return@setOnClickListener
            }

            viewModel.registrarConfiguracion(
                "jdbc:jtds:sqlserver://" + binding.etIp.text.toString().trim() +
                        ":" + binding.etPuerto.text.toString().trim() +
                        ";databaseName=dbDatos;user=" + binding.etUsuario.text.toString().trim() +
                        ";password=" + binding.etClave.text.toString().trim() + ";"
            )
        }

        viewModel.verificarConfiguracion()
    }
}