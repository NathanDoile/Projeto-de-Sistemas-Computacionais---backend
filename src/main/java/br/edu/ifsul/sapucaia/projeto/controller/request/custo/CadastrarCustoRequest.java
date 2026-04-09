package br.edu.ifsul.sapucaia.projeto.controller.request.custo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CadastrarCustoRequest {

    @NotNull
    private Long idVeiculo;

    @NotBlank
    private String tipo;

    @NotNull
    @Positive
    private double valor;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @NotBlank
    private String descricao;
}