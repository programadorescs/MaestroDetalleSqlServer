package pe.pcs.maestrodetallesqlserver.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import pe.pcs.maestrodetallesqlserver.R
import pe.pcs.maestrodetallesqlserver.core.UtilsDate
import pe.pcs.maestrodetallesqlserver.core.UtilsMessage
import pe.pcs.maestrodetallesqlserver.core.ResponseStatus
import pe.pcs.maestrodetallesqlserver.data.model.PedidoModel
import pe.pcs.maestrodetallesqlserver.databinding.FragmentReportePedidoBinding
import pe.pcs.maestrodetallesqlserver.ui.adapter.ReportePedidoAdapter
import pe.pcs.maestrodetallesqlserver.ui.viewmodel.ReportePedidoViewModel

@AndroidEntryPoint
class ReportePedidoFragment : Fragment(), ReportePedidoAdapter.IOnClickListener {

    private lateinit var binding: FragmentReportePedidoBinding
    private val viewModel: ReportePedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportePedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        binding.rvLista.adapter = ReportePedidoAdapter(this)

        viewModel.listaPedido.observe(viewLifecycleOwner) {
            (binding.rvLista.adapter as ReportePedidoAdapter).submitList(it)
        }

        viewModel.statusListaPedido.observe(viewLifecycleOwner) {
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

        viewModel.statusInt.observe(viewLifecycleOwner) {
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
                is ResponseStatus.Success -> {
                    binding.progressBar.isVisible = false

                    if (it.data > 0)
                        UtilsMessage.showToast("¡Felicidades, registro anulado correctamente!")

                    it.data = 0
                }
            }
        }

        binding.etDesde.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etDesde, parentFragmentManager)
        }

        binding.etHasta.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etHasta, parentFragmentManager)
        }

        binding.etDesde.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                buscarPorFechas()
            }
        })

        binding.etHasta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                buscarPorFechas()
            }
        })

        if (!flagRetorno) {
            UtilsDate.mostrarFechaActual(binding.etDesde)
            UtilsDate.mostrarFechaActual(binding.etHasta)
        }
    }

    companion object {
        private var flagRetorno = false
    }

    private fun buscarPorFechas() {
        if (binding.etDesde.text.toString().isNotEmpty() &&
            binding.etHasta.text.toString().isNotEmpty()
        ) {
            if (flagRetorno) {
                flagRetorno = false
            } else {
                viewModel.listarPedido(
                    binding.etDesde.text.toString(),
                    binding.etHasta.text.toString()
                )
            }
        }
    }

    override fun clickAnular(entidad: PedidoModel) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("ANULAR PEDIDO")
            setMessage("¿Esta seguro de querer anular el Pedido: ${entidad.id}?")

            setPositiveButton("SI") { dialog, _ ->
                viewModel.anularPedido(
                    entidad.id,
                    binding.etDesde.text.toString(),
                    binding.etHasta.text.toString()
                )
                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    override fun clickDetalle(entidad: PedidoModel) {
        flagRetorno = true
        viewModel.setItem(entidad)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_nav_reporte_pedido_to_reporteDetallePedidoFragment)
    }
}