package br.edu.ifsul.sapucaia.projeto.controller.request.custo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
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