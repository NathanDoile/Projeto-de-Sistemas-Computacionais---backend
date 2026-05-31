package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AlterarNotificacoesRequest {

    @NotNull(message = "Campo notificação é obrigatório")
    private String notificacao;

}
