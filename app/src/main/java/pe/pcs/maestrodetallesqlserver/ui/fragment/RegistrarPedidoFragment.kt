package pe.pcs.maestrodetallesqlserver.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.maestrodetallesqlserver.core.UtilsCommon
import pe.pcs.maestrodetallesqlserver.core.UtilsMessage
import pe.pcs.maestrodetallesqlserver.core.ResponseStatus
import pe.pcs.maestrodetallesqlserver.data.model.DetallePedidoModel
import pe.pcs.maestrodetallesqlserver.data.model.PedidoModel
import pe.pcs.maestrodetallesqlserver.databinding.FragmentRegistrarPedidoBinding
import pe.pcs.maestrodetallesqlserver.ui.adapter.CarritoAdapter
import pe.pcs.maestrodetallesqlserver.ui.viewmodel.PedidoViewModel
import java.sql.Date
import java.util.Calendar


@AndroidEntryPoint
class RegistrarPedidoFragment : Fragment(), CarritoAdapter.IOnClickListener {

    private lateinit var binding: FragmentRegistrarPedidoBinding
    private val viewModel: PedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrarPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        binding.rvLista.adapter = CarritoAdapter(this)

        viewModel.listaCarrito.observe(viewLifecycleOwner) {
            (binding.rvLista.adapter as CarritoAdapter).setData(it)
        }

        viewModel.totalImporte.observe(viewLifecycleOwner) {
            binding.tvImporte.text = UtilsCommon.formatearDoubleString(it)
        }

        viewModel.statusInt.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> binding.progressBar.isVisible = true
                is ResponseStatus.Error -> {
                    binding.progressBar.isVisible = false

                    if (it.message.isNotEmpty())
                        UtilsMessage.showAlertOk(
                            "ERROR", it.message, requireContext()
                        )

                    it.message = ""
                }
                is ResponseStatus.Success -> {
                    binding.progressBar.isVisible = false

                    if (it.data > 0) {
                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle("CONFORME")
                            setMessage("¡El pedido fue registrado correctamente!")
                            setCancelable(false)
                            setPositiveButton("Aceptar") { dialog, _ ->

                                viewModel.limpiarCarrito()

                                binding.etCliente.setText("")

                                Navigation.findNavController(requireView()).popBackStack()
                                dialog.cancel()
                            }
                        }.create().show()
                    }

                    it.data = 0
                }
            }
        }

        binding.fabCarrito.setOnClickListener {
            UtilsCommon.ocultarTeclado(it)

            if (!viewModel.listaCarrito.value.isNullOrEmpty()) {
                val pedido = PedidoModel().apply {
                    fecha = Date(Calendar.getInstance().time.time)
                    total = viewModel.totalImporte.value!!
                    cliente = binding.etCliente.text.toString().trim().ifEmpty { "Publico general" }
                    estado = "Vigente"
                    detalles = viewModel.listaCarrito.value!!
                }

                viewModel.registrarPedido(pedido)
            } else {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA",
                    "No exsite items en el carrito",
                    requireContext()
                )
            }
        }

        viewModel.listarCarrito()
    }

    override fun clickMas(entidad: DetallePedidoModel) {
        viewModel.setAumentarCantidadProducto(entidad)
    }

    override fun clickMenos(entidad: DetallePedidoModel) {
        viewModel.setDisminuirCantidadProducto(entidad)
    }

    override fun clickEliminar(entidad: DetallePedidoModel) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("QUITAR")
            setMessage("¿Desea quitar el registro: ${entidad.mProducto?.descripcion}?")

            setPositiveButton("SI") { dialog, _ ->
                viewModel.quitarProductoCarrito(entidad)

                if (viewModel.listaCarrito.value?.size!! == 0) {
                    binding.etCliente.setText("")
                    Navigation.findNavController(requireView()).popBackStack()
                }

                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }
}