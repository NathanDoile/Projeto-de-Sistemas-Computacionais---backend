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
public class AlterarNotificacaoVencimentoRequest {

    @NotNull(message = "Campo notificação do Vencimento é obrigatório")
    private Boolean notificacaoVencimento;
}