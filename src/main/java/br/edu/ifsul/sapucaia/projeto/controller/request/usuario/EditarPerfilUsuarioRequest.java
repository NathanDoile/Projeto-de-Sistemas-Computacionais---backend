package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.Email;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_EMAIL;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EditarPerfilUsuarioRequest {

    private String nome;

    @Email(message = CAMPO_EMAIL)
    private String email;

    private String telefone;
    
}
