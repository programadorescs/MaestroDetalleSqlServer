package pe.pcs.maestrodetallesqlserver.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import pe.pcs.maestrodetallesqlserver.core.UtilsMessage
import pe.pcs.maestrodetallesqlserver.databinding.ActivityInicioBinding
import pe.pcs.maestrodetallesqlserver.ui.dialog.ConfigServerDialog
import pe.pcs.maestrodetallesqlserver.ui.viewmodel.ConfigServerViewModel

class InicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    private val viewModel: ConfigServerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.msgError.observe(this) {
            if (it.isEmpty()) return@observe

            UtilsMessage.showAlertOk(
                "ERROR", it, this
            )

            viewModel.limpiarError()
        }

        viewModel.msgExito.observe(this) {
            if (it.isEmpty()) return@observe

            if (it.lowercase() != "configurado") return@observe
            viewModel.limpiarExito()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.tvConfigurarServidor.setOnClickListener {
            ConfigServerDialog.newInstance("Configurar servidor")
                .show(supportFragmentManager, "IPServer")
        }

        binding.btIngresar.setOnClickListener {
            viewModel.verificarConfiguracion()
        }
    }
}