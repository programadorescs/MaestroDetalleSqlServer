package pe.pcs.maestrodetallesqlserver.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.maestrodetallesqlserver.core.UtilsCommon
import pe.pcs.maestrodetallesqlserver.core.UtilsMessage
import pe.pcs.maestrodetallesqlserver.core.ResponseStatus
import pe.pcs.maestrodetallesqlserver.databinding.FragmentReporteDetallePedidoBinding
import pe.pcs.maestrodetallesqlserver.ui.adapter.ReporteDetallePedidoAdapter
import pe.pcs.maestrodetallesqlserver.ui.viewmodel.ReportePedidoViewModel


@AndroidEntryPoint
class ReporteDetallePedidoFragment : Fragment() {

    private lateinit var binding: FragmentReporteDetallePedidoBinding
    private val viewModel: ReportePedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReporteDetallePedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        viewModel.listaDetalle.observe(viewLifecycleOwner) {
            binding.rvLista.adapter = it?.let { it1 -> ReporteDetallePedidoAdapter(it1) }
        }

        viewModel.statusListaDetalle.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Error -> {
                    binding.progressBar.isVisible = false

                    if (it.message.isNotEmpty())
                        UtilsMessage.showAlertOk(
                            "ERROR", it.message, requireContext()
                        )

                    it.message = ""
                }
                is ResponseStatus.Loading -> binding.progressBar.isVisible = true
                is ResponseStatus.Success -> binding.progressBar.isVisible = false
            }
        }

        viewModel.itemPedido.observe(viewLifecycleOwner) {
            if (viewModel.itemPedido.value != null) {
                binding.tvPedido.text = "Pedido: ${viewModel.itemPedido.value?.id ?: 0}"
                binding.tvImporte.text =
                    UtilsCommon.formatearDoubleString(viewModel.itemPedido.value?.total ?: 0.0)
            }
        }

        viewModel.listarDetalle(viewModel.itemPedido.value?.id ?: 0)
    }
}