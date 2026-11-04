package com.example.orcafacil

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.orcafacil.databinding.ActivityOrcamentoBinding

class OrcamentoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityOrcamentoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnCalcular.setOnClickListener {
            validarFormulario()
        }
    }
    private fun validarFormulario(): Boolean {

        val descricao = binding.etDescricao.text.toString().trim()

        if (descricao.isEmpty()) {
            binding.etDescricao.error = getString(R.string.erro_descricao_obrigatoria)
            binding.etDescricao.requestFocus()
            return false
        }
        binding.etDescricao.error = null

        val horas = binding.etHoras.text.toString().trim().replace(',','.')
            .toDoubleOrNull()

        if (horas == null || !horas.isFinite() || horas <= 0){
            binding.etHoras.error = getString(R.string.erro_horas_invalidas)
            binding.etHoras.requestFocus()
            return false
        }

        binding.etHoras.error = null

        val precoHora = binding.etPrecoHora.text.toString().trim().replace(',',
            '.').toDoubleOrNull()

        if (precoHora == null || !precoHora.isFinite() || precoHora <= 0) {
            binding.etPrecoHora.error = getString(R.string.erro_preco_hora_invalido)
            binding.etPrecoHora.requestFocus()
            return false
        }

        binding.etPrecoHora.error = null

        val despesas = binding.etDespesas.text.toString().trim().replace(','
            ,'.').toDoubleOrNull()

        if (despesas == null || !despesas.isFinite() || despesas <= 0) {
            binding.etDespesas.error = getString(R.string.erro_despesas_invalidas)
            binding.etDespesas.requestFocus()
            return false
        }

        return true
    }
}