package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CadastrarMetaRequest {

    @NotBlank
    private String titulo;

    @NotNull
    private String formato;

    @NotNull
    private Double valor;

    @NotNull
    private Long idUsuario;
}
