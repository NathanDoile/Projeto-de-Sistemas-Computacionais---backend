package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.Email;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EditarPerfilUsuarioRequest {

    private String nome;

    @Email(message = "não é um endereço válido.")
    private String email;

    private String telefone;
    
}
