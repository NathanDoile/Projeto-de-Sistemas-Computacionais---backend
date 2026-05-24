package br.edu.ifsul.sapucaia.projeto.service.ia;

import br.edu.ifsul.sapucaia.projeto.controller.request.ia.*;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.IaResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.exception.IAException;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaManutencaoIAResponseValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaPossiveisCamposNullRespostaIAValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class IAService {
    @Value("${gemini.baseurl}") String baseUrl;
    @Value("${gemini.api.key}") String apiKey;
    private static final String INSTRUCAO = "Você é um assistente técnico especializado em manutenção veicular.\\n\\nDIRETRIZES DE CÁLCULO:\\n1. IDENTIFIQUE o intervalo de manutenção específico do fabricante para USO SEVERO (trânsito intenso) usando a busca. Este intervalo é geralmente menor que o de uso normal (ex: 5k em vez de 10k km).\\n2. USE este intervalo exato. Não assuma 10k por padrão.\\n\\nFLUXO:\\n1. Receba: marca, modelo, ano e quilometragem atual\\n2. Busque o intervalo de manutenção oficial para USO SEVERO (trânsito intenso) — sites da montadora têm prioridade\\n\\nFORMATO DE RESPOSTA (JSON puro):\\n{\\n  \\\"veiculo\\\": {\\n    \\\"marca\\\": \\\"string\\\",\\n    \\\"modelo\\\": \\\"string\\\",\\n    \\\"ano\\\": \\\"string\\\",\\n    \\\"quilometragem_atual\\\": number\\n  },\\n  \\\"proxima_revisao\\\": {\\n    \\\"intervalo_manutencoes_km\\\": number,\\n    \\\"intervalo_manutencoes_meses\\\": number,\\n    \\\"distancia_restante_km\\\": number,\\n    \\\"erro\\\": \\\"string\\\"\\n  }\\n}";

    private final ValidaPossiveisCamposNullRespostaIAValidator validaPossiveisCamposNullRespostaIAValidator;

    private final ValidaManutencaoIAResponseValidator validaManutencaoIAResponseValidator;

    private final ObjectMapper objectMapper;

    public ManutencaoIAResponse chamadaChat(String mensagem){

        int tentativas = 0;
        int maxTentativas = 2;
        long tempoEsperaEntreTentativas = 5000;

        while (true) {
            try {
                return fazerChamada(mensagem);
            }catch(ResponseStatusException e){
                throw e;
            }
            catch (Exception e) {
                tentativas++;
                if (tentativas >= maxTentativas) {
                    throw new IAException("IA indisponível após " + tentativas + " tentativas", e);
                }
            }
            try {
                Thread.sleep(tempoEsperaEntreTentativas);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new IAException("Interrupção ao tentar novamente", ie);
            }
        }
    }

    private ManutencaoIAResponse fazerChamada(String mensagem){

        RestClient restClient = RestClient.create();
        String apiUrl = String.format(baseUrl, apiKey);
        String responseMimeType = "application/json";
        int temperature = 0;

        var requestBody = new BodyRequest(new SystemInstructionRequest(List.of(new PartInstructionRequest(INSTRUCAO)))
                , List.of(new ContentRequest(List.of(new PartRequest(mensagem)))),
                (new GenerationConfigRequest(responseMimeType, temperature))
        );

        String requesBodyJson = objectMapper.writeValueAsString(requestBody);

        ResponseEntity<String> response = restClient
                .post()
                .uri(apiUrl)
                .contentType(APPLICATION_JSON)
                .body(requesBodyJson)
                .retrieve()
                .toEntity(String.class);

        IaResponse iaResponse = objectMapper.readValue(
                response.getBody(), IaResponse.class
        );

        validaPossiveisCamposNullRespostaIAValidator.validaNotNull(iaResponse);

        String resposta = iaResponse
                .candidates().get(0)
                .content()
                .parts().get(0)
                .text();

        ManutencaoIAResponse respostaManutencao = objectMapper.readValue(resposta, ManutencaoIAResponse.class);

        validaManutencaoIAResponseValidator.validaDadosResposta(respostaManutencao);

        return respostaManutencao;
    }
}
