package br.edu.ifsul.sapucaia.projeto.security.controller.request;

import jakarta.validation.constraints.*;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ValidarCodigoRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    @Email(message = CAMPO_EMAIL)
    private String email;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    @Size(min = CAMPO_TAMANHO_MAXIMO_MINIMO_CODIGO_RCUPERAR_SENHA, max = CAMPO_TAMANHO_MAXIMO_MINIMO_CODIGO_RCUPERAR_SENHA, message = CAMPO_CODIGO_RECUPERAR_SENHA)
    private String codigo;
    
}