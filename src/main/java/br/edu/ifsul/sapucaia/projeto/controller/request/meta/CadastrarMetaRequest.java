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

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @NotBlank(message = "O formato é obrigatório.")
    private String formato;

    @NotBlank(message = "O tipo é obrigatório.")
    private String tipo;

    @NotNull(message = "O valor é obrigatório.")
    private Double valor;
}