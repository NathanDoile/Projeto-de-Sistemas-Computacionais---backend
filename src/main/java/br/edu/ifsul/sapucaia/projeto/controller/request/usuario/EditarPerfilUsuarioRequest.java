package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EditarPerfilUsuarioRequest {

    private String nome;

    @Email(message = "não é um endereço válido.")
    private String email;

    private String telefone;
    
}
