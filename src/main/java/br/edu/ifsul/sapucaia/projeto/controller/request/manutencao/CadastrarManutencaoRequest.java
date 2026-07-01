package br.edu.ifsul.sapucaia.projeto.controller.request.manutencao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;
import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_POSITIVO;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CadastrarManutencaoRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String tipo;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private LocalDate dataManutencao;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String descricao;

    @NotNull(message = CAMPO_OBRIGATORIO)
    @Positive(message = CAMPO_POSITIVO)
    private double valor;
}