package com.example.orcafacil

import kotlin.math.round

data class ResultadoOrcamento(
    val custoServico : Double,
    val despesas: Double,
    val total: Double
)

fun calcularOrcamento(horas: Double, precoHora: Double, despesas: Double) : ResultadoOrcamento {
        val custoServico = arredondar(horas * precoHora)
        val despesasArredondadas = arredondar(despesas)
        val total = arredondar(custoServico + despesasArredondadas)

        return ResultadoOrcamento(custoServico, despesasArredondadas, total)
    }

private fun arredondar(valor: Double) : Double {
        return round(valor * 100.0) / 100.0
    }