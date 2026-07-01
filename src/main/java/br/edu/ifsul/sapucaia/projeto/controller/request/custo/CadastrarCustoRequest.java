package br.edu.ifsul.sapucaia.projeto.controller.request.custo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;
import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_POSITIVO;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastrarCustoRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String tipo;

    @NotNull(message = CAMPO_OBRIGATORIO)

    @Positive(message = CAMPO_POSITIVO)
    private double valor;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String descricao;
}