package br.edu.ifsul.sapucaia.projeto.controller.request.custo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditarCustoRequest {

    @NotNull(message = CAMPO_OBRIGATORIO)
    private Long idCusto;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String tipo;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private double valor;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private LocalDate dataVencimento;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private LocalDate dataPagamento;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String descricao;
}