package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AlterarNotificacaoManutencaoRequest {

    @NotNull(message = "Campo notificação da Manutencao é obrigatório")
    private Boolean notificacaoManutencao;
}