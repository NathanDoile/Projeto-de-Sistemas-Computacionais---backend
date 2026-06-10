package br.edu.ifsul.sapucaia.projeto.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class RedefinirSenhaRequest {

    @NotBlank(message = "Digite sua nova senha.")
    private String senha;

    @NotBlank(message = "Digite seu e-mail.")
    private String email;

    @NotBlank(message = "Informe o código de redefinição enviado para seu e-mail.")
    private String codigo;

}