package br.edu.ifsul.sapucaia.projeto.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class EnviarCodigoRequest {

    @NotBlank(message = "Digite seu e-mail.")
    private String email;

}
