package br.edu.ifsul.sapucaia.projeto.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto.IMPOSTOS;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaTipoCustoValidatorTest {

    @InjectMocks
    private ValidaTipoCustoValidator tested;

    @Test
    @DisplayName("Não deve dar erro se tipo de custo for válido")
    void naoDeveDarErroSeTipoCustoValido(){

        String tipo = IMPOSTOS.getDescricao();

        assertDoesNotThrow(() -> tested.tipoValido(tipo));
    }

    @Test
    @DisplayName("Deve dar erro se tipo de custo for nulo ou vazio")
    void deveDarErroSeTipoCustoNulo(){

        String tipoI = null;

        ResponseStatusException exceptionI = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipoI));

        assertEquals(BAD_REQUEST, exceptionI.getStatusCode());
        assertEquals("Campo tipo é obrigatório.", exceptionI.getReason());

        String tipoII = "               ";

        ResponseStatusException exceptionII = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipoII));

        assertEquals(BAD_REQUEST, exceptionII.getStatusCode());
        assertEquals("Campo tipo é obrigatório.", exceptionII.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se tipo de custo for invalido")
    void deveDarErroSeTipoCustoInvalido(){

        String tipo = "tipoinvalido";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Tipo de custo inválido. Tipos aceitos: Manutenção, Combustível, Seguro, Impostos, Outros.", exception.getReason());
    }
}
