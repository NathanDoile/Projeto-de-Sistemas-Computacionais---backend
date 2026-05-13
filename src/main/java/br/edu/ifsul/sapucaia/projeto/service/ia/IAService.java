package br.edu.ifsul.sapucaia.projeto.service.ia;

import br.edu.ifsul.sapucaia.projeto.controller.request.ia.*;
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

    public String chamadaChat(String instrucao, String mensagem){
        RestClient restClient = RestClient.create();
        String apiUrl = String.format(baseUrl, apiKey);

        var requestBody = new BodyRequest(new SystemInstructionRequest(List.of(new PartInstructionRequest(instrucao)))
                    ,List.of(new ContentRequest(List.of(new PartRequest(mensagem))))
        );

        try{
            var objectMapper = new ObjectMapper();
            String requesBodyJson = objectMapper.writeValueAsString(requestBody);

            ResponseEntity<String> response = restClient
                    .post()
                    .uri(apiUrl)
                    .contentType(APPLICATION_JSON)
                    .body(requesBodyJson)
                    .retrieve()
                    .toEntity(String.class);

            return response.getBody();

        }catch (RestClientException e){
            throw new RuntimeException("Erro na chamada para a API do Gemini", e);
        }
    }
}
