package br.edu.ifsul.sapucaia.projeto.controller.request.custo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EditarCustoRequest {

    @NotNull
    private Long idCusto;

    @NotBlank
    private String tipo;

    @NotNull
    private double valor;

    @NotNull
    private LocalDate dataVencimento;

    @NotNull
    private LocalDate dataPagamento;

    @NotBlank
    private String descricao;
}
