package br.edu.ifsul.sapucaia.projeto.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoNotificacao.VENCIMENTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaTipoNotificacaoValidatorTest {
    @InjectMocks
    private ValidaTipoNotificacaoValidator tested;

    @Test
    @DisplayName("Não deve dar erro se tipo de notificacao for valido")
    void naoDeveDarErroSeTipoNotificacaoValido(){

        String tipo = VENCIMENTO.getDescricao();

        assertDoesNotThrow(() -> tested.tipoValido(tipo));
    }

    @Test
    @DisplayName("Deve dar erro se tipo de notificação for nulo ou vazio")
    void deveDarErroSeTipoNotificacaoNuloOuVazio(){

        String tipoI = null;

        ResponseStatusException exceptionI = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipoI));

        assertEquals(BAD_REQUEST, exceptionI.getStatusCode());
        assertEquals("O tipo de notificação é obrigatório.", exceptionI.getReason());

        String tipoII = "   ";

        ResponseStatusException exceptionII = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipoII));

        assertEquals(BAD_REQUEST, exceptionII.getStatusCode());
        assertEquals("O tipo de notificação é obrigatório.", exceptionII.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se tipo de notificação for invalido")
    void deveDarErroSeTipoNotificacaoInvalido(){

        String tipo = "tipoinvalido";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Tipo de Notificação inválido. Tipos aceitos: Manutenção, Vencimento.", exception.getReason());
    }
}
