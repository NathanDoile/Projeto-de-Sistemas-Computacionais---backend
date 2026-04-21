package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AlterarNotificacaoVencimentoRequest {

    @NotNull(message = "Campo notificação do Vencimento é obrigatório")
    private Boolean notificacaoVencimento;
}