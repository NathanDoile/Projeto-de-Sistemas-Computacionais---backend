package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AlterarSenhaUsuarioRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String senhaAtual;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String novaSenha;
}
