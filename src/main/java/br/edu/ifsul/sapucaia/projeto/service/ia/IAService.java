package br.edu.ifsul.sapucaia.projeto.service.ia;

import br.edu.ifsul.sapucaia.projeto.controller.request.ia.*;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.IaResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.exception.IAException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class IAService {
    @Value("${gemini.baseurl}") String baseUrl;
    @Value("${gemini.api.key}") String apiKey;
    private static final String instrucao = "Você é um assistente técnico especializado em manutenção veicular.\\n\\nDIRETRIZES DE CÁLCULO:\\n1. IDENTIFIQUE o intervalo de manutenção específico do fabricante (ex: 10k, 12k, 15k ou 20k km) usando a busca.\\n2. USE este intervalo exato para calcular a progressão das revisões. Não assuma 10k por padrão.\\n3. CALCULE a próxima revisão como o primeiro múltiplo do intervalo que seja maior que a quilometragem atual.\\n\\nDIRETRIZES DE COMPONENTES:\\n4. Inclua APENAS os itens que são substituídos em ABSOLUTAMENTE TODAS as paradas de manutenção, sem exceção. Se um item pula qualquer revisão, NÃO o inclua.\\n5. NÃO inclua itens com qualquer ciclo maior que o intervalo base (ex: filtro de cabine a cada 20k, filtro de ar a cada 40k, velas, correias, fluidos).\\n6. Para motores FLEX, verifique explicitamente se o filtro de combustível é exigido em todas as revisões — em muitos modelos Flex ele tem troca obrigatória a cada intervalo base.\\n\\nFLUXO:\\n1. Receba: marca, modelo, ano e quilometragem atual\\n2. Busque o plano de manutenção oficial (sites da montadora têm prioridade)\\n3. Filtre apenas revisões futuras (km_revisao >= km_atual) para o uso normal do veículo\\n4. Faça as próximas 3 manutenções.\\n\\nFORMATO DE RESPOSTA (JSON puro):\\n{\\n  \\\"veiculo\\\": {\\n    \\\"marca\\\": \\\"string\\\",\\n    \\\"modelo\\\": \\\"string\\\",\\n    \\\"ano\\\": \\\"string\\\",\\n    \\\"quilometragem_atual\\\": number\\n  },\\n  \\\"proximas_revisoes\\\": [\\n    {\\n      \\\"revisao_km\\\": number,\\n      \\\"tempo_entre_manutencao_meses\\\": number,\\n      \\\"distancia_restante_km\\\": number,\\n      \\\"itens\\\": [\\n        { \\\"componente\\\": \\\"string\\\" }\\n      ]\\n    }\\n  ],\\n  \\\"erro\\\": \\\"string\\\"\\n}";

    public ManutencaoIAResponse chamadaChat(String mensagem){
        RestClient restClient = RestClient.create();
        String apiUrl = String.format(baseUrl, apiKey);
        String responseMimeType = "application/json";
        int temperature = 0;

        try{
            var requestBody = new BodyRequest(new SystemInstructionRequest(List.of(new PartInstructionRequest(instrucao)))
                    ,List.of(new ContentRequest(List.of(new PartRequest(mensagem)))),
                    (new GenerationConfigRequest(responseMimeType, temperature))
            );
            var objectMapper = new ObjectMapper();
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

            String resposta = iaResponse
                    .candidates().get(0)
                    .content()
                    .parts().get(0)
                    .text();

            return objectMapper.readValue(resposta, ManutencaoIAResponse.class);

        }catch (RestClientException e){
            throw new IAException("Erro na chamada para a API do Gemini", e);
        }
    }
}
