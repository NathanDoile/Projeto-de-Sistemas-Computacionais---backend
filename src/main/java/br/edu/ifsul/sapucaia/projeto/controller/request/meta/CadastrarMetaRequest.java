package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
