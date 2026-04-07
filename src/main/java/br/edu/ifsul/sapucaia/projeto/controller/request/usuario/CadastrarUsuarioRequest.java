package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CadastrarUsuarioRequest {

    @NotNull
    private String nome;

    @NotNull(message = "é um campo obrigatório")
    @Email(message = "não é um endereço válido.")
    private String email;

    @NotNull(message = "é um campo obrigatório")
    private String senha;

    @NotNull(message = "é um campo obrigatório")
    private LocalDate dataCadastro;

    @NotNull(message = "é um campo obrigatório")
    private String telefone;
}
