package br.edu.ifsul.sapucaia.projeto.security.controller.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ValidarCodigoRequest {

    @NotBlank(message = "Digite seu e-mail.")
    @Email(message = "Insira um e-mail válido.")
    private String email;

    @NotBlank(message = "O código não pode estar em branco.")
    @Size(min = 8, max = 8, message = "O código deve conter exatamente 8 caracteres.")
    private String codigo;
    
}