package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastrarMetaRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String titulo;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String formato;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String tipo;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private Double valor;
}