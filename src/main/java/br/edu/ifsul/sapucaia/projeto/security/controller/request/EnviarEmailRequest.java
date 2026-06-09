package br.edu.ifsul.sapucaia.projeto.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class EnviarEmailRequest {

    @NotBlank(message = "Campo obrigatório.")
    private String assunto;

    @NotBlank(message = "Campo obrigatório.")
    private String conteudo;

}