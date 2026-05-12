package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
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
public class ValidaTipoCustoValidatorTest {

    @InjectMocks
    private ValidaTipoCustoValidator tested;

    @Test
    @DisplayName("Não deve dar erro se tipo de custo for válido")
    void naoDeveDarErroSeTipoCustoValido(){

        String tipo = IMPOSTOS.getDescricao();

        assertDoesNotThrow(() -> tested.tipoValido(tipo));
    }

    @Test
    @DisplayName("Deve dar erro se tipo de custo for nulo")
    void deveDarErroSeTipoCustoNulo(){

        String tipo = null;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Campo tipo é obrigatório.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se tipo de custo for vazio")
    void deveDarErroSeTipoCustoVazio(){

        String tipo = "               ";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Campo tipo é obrigatório.", exception.getReason());
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
