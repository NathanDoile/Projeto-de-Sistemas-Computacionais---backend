package br.edu.ifsul.sapucaia.projeto.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class EnviarEmailRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String assunto;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String conteudo;

}