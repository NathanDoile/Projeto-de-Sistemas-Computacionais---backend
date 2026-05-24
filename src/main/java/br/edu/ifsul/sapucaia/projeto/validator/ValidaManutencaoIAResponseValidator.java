package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@Component
public class ValidaManutencaoIAResponseValidator {
    public void validaDadosResposta(ManutencaoIAResponse resposta) {
        if (resposta.proximaRevisao() == null) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Resposta da IA sem dados de próxima revisão");
        }

        if (resposta.proximaRevisao().intervaloManutencoesKm() <= 0) {
            throw new ResponseStatusException(UNPROCESSABLE_CONTENT, "Intervalo de manutenção inválido: " + resposta.proximaRevisao().intervaloManutencoesKm());
        }
    }
}
