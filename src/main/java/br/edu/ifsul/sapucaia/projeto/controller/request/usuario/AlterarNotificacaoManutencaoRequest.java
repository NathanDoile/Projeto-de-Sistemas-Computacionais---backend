package br.edu.ifsul.sapucaia.projeto.controller.request.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AlterarNotificacaoManutencaoRequest {

    @NotNull(message = "Campo notificação da Manutencao é obrigatório")
    private Boolean notificacaoManutencao;
}