package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.controller.response.ia.IaResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Component
public class ValidaPossiveisCamposNullRespostaIAValidator {
    public void validaNotNull(IaResponse iaResponse) {
        if (iaResponse == null || iaResponse.candidates() == null || iaResponse.candidates().isEmpty()) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Resposta da IA veio vazia ou sem candidatos");
        }

        var candidate = iaResponse.candidates().get(0);

        if (candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Candidato da IA sem conteúdo/partes");
        }

        String texto = candidate.content().parts().get(0).text();
        if (texto == null || texto.isBlank()) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Texto da resposta da IA está vazio");
        }
    }
}
