package br.edu.ifsul.sapucaia.projeto.controller.request.custo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastrarCustoRequest {

    @NotNull(message = "O ID do veículo é obrigatório.")
    private Long idVeiculo;

    @NotBlank(message = "O tipo é obrigatório.")
    private String tipo;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser maior que zero.")
    private double valor;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;
}