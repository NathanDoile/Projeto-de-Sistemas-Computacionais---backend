package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ExcluirContaUsuarioRequest {

    @NotBlank(message = "Você deve informar a senha para excluir a conta.")
    private String senha;
}
