package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CadastrarUsuarioRequest {

    @NotBlank(message = "Campo nome é obrigatório")
    private String nome;

    @NotBlank(message = "Campo E-mail é obrigatório")
    @Email(message = "não é um endereço válido.")
    private String email;

    @NotBlank(message = "Campo senha é obrigatório")
    private String senha;

    @NotBlank(message = "Campo telefone é obrigatório")
    private String telefone;
}
