package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ExcluirContaUsuarioRequest {

    @NotBlank(message = "Você deve informar a senha para excluir a conta.")
    private String senha;
}
