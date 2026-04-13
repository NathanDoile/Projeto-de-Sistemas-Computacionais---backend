package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AlterarSenhaUsuarioRequest {

    @NotBlank(message = "Campo senha antiga é obrigatório")
    private String senhaAtual;

    @NotBlank(message = "Campo nova senha é obrigatório")
    private String novaSenha;
}
