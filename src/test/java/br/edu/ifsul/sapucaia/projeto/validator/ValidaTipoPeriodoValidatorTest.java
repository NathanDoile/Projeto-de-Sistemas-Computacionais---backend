package br.edu.ifsul.sapucaia.projeto.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaTipoPeriodoValidatorTest {

    @InjectMocks
    private ValidaTipoPeriodoValidator tested;

    @Test
    @DisplayName("Deve retornar erro se tipo for invalido")
    void deveRetornarErroSeTipoInvalido(){

        String tipo = "invalido";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porTipo(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Tipo dde período inválido. Aceito apenas tipos: dia, semana, mes e ano.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve retornar erro se tipo for valido")
    void naoDeveRetornarErroSeTipoValido(){

        String tipoI = "dia";
        String tipoII = "semana";
        String tipoIII = "mes";
        String tipoIV = "ano";

        assertDoesNotThrow(() -> tested.porTipo(tipoI));
        assertDoesNotThrow(() -> tested.porTipo(tipoII));
        assertDoesNotThrow(() -> tested.porTipo(tipoIII));
        assertDoesNotThrow(() -> tested.porTipo(tipoIV));
    }
}
