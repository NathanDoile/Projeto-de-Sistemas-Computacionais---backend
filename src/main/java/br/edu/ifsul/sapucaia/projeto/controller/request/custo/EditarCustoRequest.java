package br.edu.ifsul.sapucaia.projeto.controller.request.custo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditarCustoRequest {

    @NotNull(message = "O ID do custo é obrigatório.")
    private Long idCusto;

    @NotBlank(message = "O tipo do custo é obrigatório.")
    private String tipo;

    @NotNull(message = "O valor do custo é obrigatório.")
    private double valor;

    @NotNull(message = "A data de vencimento é obrigatória.")
    private LocalDate dataVencimento;

    @NotNull(message = "A data de pagamento é obrigatória.")
    private LocalDate dataPagamento;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;
}