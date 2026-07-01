package br.edu.ifsul.sapucaia.projeto.security.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_EMAIL;
import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class EnviarCodigoRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    @Email(message = CAMPO_EMAIL)
    private String email;

}