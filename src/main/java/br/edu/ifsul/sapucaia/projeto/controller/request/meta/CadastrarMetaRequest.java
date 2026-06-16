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

    @NotBlank(message = "O título da meta é obrigatório.")
    private String titulo;

    @NotBlank(message = "O formato da meta é obrigatório.")
    private String formato;

    @NotBlank(message = "O tipo da meta é obrigatório.")
    private String tipo;

    @NotNull(message = "O valor da meta é obrigatório.")
    private Double valor;
}