package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.controller.response.ia.*;
import br.edu.ifsul.sapucaia.projeto.factory.IAFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ExtendWith(MockitoExtension.class)
class ValidaPossiveisCamposNullRespostaValidatorTest {

    @InjectMocks
    private ValidaPossiveisCamposNullRespostaIAValidator tested;

    @Test
    @DisplayName("Não deve dar erro se resposta da proxima revisao for diferente de null e intervalo entre manutenções Km for positivo")
    void naoDeveDarErroRespostaNotNull(){
        IaResponse resposta = IAFactory.responseIA();
        assertDoesNotThrow(() -> tested.validaNotNull(resposta));
    }

    @Test
    @DisplayName("Deve dar erro se a resposta for null ou vazia")
    void deveDarErroRespostaNullVazia(){

        IaResponse respostaI = new IaResponse(null);

        ResponseStatusException exceptionI = assertThrows(ResponseStatusException.class, () -> tested.validaNotNull(respostaI));

        assertEquals(SERVICE_UNAVAILABLE, exceptionI.getStatusCode());
        assertEquals("Resposta da IA veio vazia ou sem candidatos", exceptionI.getReason());

        IaResponse respostaII = new IaResponse(List.of());

        ResponseStatusException exceptionII = assertThrows(ResponseStatusException.class, () -> tested.validaNotNull(respostaII));

        assertEquals(SERVICE_UNAVAILABLE, exceptionII.getStatusCode());
        assertEquals("Resposta da IA veio vazia ou sem candidatos", exceptionII.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se o content(conteudo) da resposta for null ou vazio")
    void deveDarErroContentNullVazia(){

        IaResponse respostaI = new IaResponse(List.of(new CandidateResponse(new ContentResponse(null))));

        ResponseStatusException exceptionI = assertThrows(ResponseStatusException.class, () -> tested.validaNotNull(respostaI));

        assertEquals(SERVICE_UNAVAILABLE, exceptionI.getStatusCode());
        assertEquals("Candidato da IA sem conteúdo/partes", exceptionI.getReason());

        IaResponse respostaII = new IaResponse(List.of(new CandidateResponse(new ContentResponse(List.of()))));

        ResponseStatusException exceptionII = assertThrows(ResponseStatusException.class, () -> tested.validaNotNull(respostaII));

        assertEquals(SERVICE_UNAVAILABLE, exceptionII.getStatusCode());
        assertEquals("Candidato da IA sem conteúdo/partes", exceptionII.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se o texto da resposta for null ou vazio")
    void deveDarErroTextoNullVazio(){

        IaResponse respostaI = new IaResponse(List.of(new CandidateResponse(new ContentResponse(List.of(new PartResponse(null))))));

        ResponseStatusException exceptionI = assertThrows(ResponseStatusException.class, () -> tested.validaNotNull(respostaI));

        assertEquals(SERVICE_UNAVAILABLE, exceptionI.getStatusCode());
        assertEquals("Texto da resposta da IA está vazio", exceptionI.getReason());

        IaResponse respostaII = new IaResponse(List.of(new CandidateResponse(new ContentResponse(List.of(new PartResponse(""))))));

        ResponseStatusException exceptionII = assertThrows(ResponseStatusException.class, () -> tested.validaNotNull(respostaII));

        assertEquals(SERVICE_UNAVAILABLE, exceptionII.getStatusCode());
        assertEquals("Texto da resposta da IA está vazio", exceptionII.getReason());
    }
}
