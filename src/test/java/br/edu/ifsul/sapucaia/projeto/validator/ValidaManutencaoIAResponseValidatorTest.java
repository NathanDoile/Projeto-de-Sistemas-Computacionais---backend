package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ProximaRevisaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.factory.IAFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class ValidaManutencaoIAResponseValidatorTest {

    @InjectMocks
    private ValidaManutencaoIAResponseValidator tested;

    @Test
    @DisplayName("Não deve dar erro se resposta da proxima revisao for diferente de null e intervalo entre manutenções Km for positivo")
    void naoDeveDarErroRespostaNotNull(){
        ManutencaoIAResponse resposta = IAFactory.response();
        assertDoesNotThrow(() -> tested.validaDadosResposta(resposta));
    }

    @Test
    @DisplayName("Deve dar erro se a resposta da proxima revisao for null")
    void deveDarErroRespostaNull(){

        ManutencaoIAResponse resposta = new ManutencaoIAResponse(null, null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.validaDadosResposta(resposta));

        assertEquals(SERVICE_UNAVAILABLE, exception.getStatusCode());
        assertEquals("Resposta da IA sem dados de próxima revisão", exception.getReason());

    }

    @Test
    @DisplayName("Deve dar erro se a quilometragem entre manutenções for negativa")
    void deveDarErroRespostaNegativa(){

        ManutencaoIAResponse resposta = new ManutencaoIAResponse(null, new ProximaRevisaoIAResponse(-5000, 6, 5200));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.validaDadosResposta(resposta));

        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertEquals("Intervalo de manutenção inválido: " + resposta.proximaRevisao().intervaloManutencoesKm(), exception.getReason());

    }
}
