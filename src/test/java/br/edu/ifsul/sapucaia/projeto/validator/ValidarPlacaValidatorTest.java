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
class ValidarPlacaValidatorTest {

    @InjectMocks
    private ValidarPlacaValidator tested;

    @Test
    @DisplayName("Não deve dar erro se placa estiver correta")
    void naoDeveDarErroSePlacaCorreta(){

        String placa = "ABC1D23";

        assertDoesNotThrow(() -> tested.formatoValido(placa));
    }

    @Test
    @DisplayName("Deve dar erro se placa invalida")
    void deveDarErroSePlacaInvalida(){

        String placaI = "A2C1D23";

        String placaII = "ABCED23";

        String placaIII = "ABC1#23";

        String placaIV = "ABC1D2E";

        ResponseStatusException exceptionI = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placaI));
        ResponseStatusException exceptionII = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placaII));
        ResponseStatusException exceptionIII = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placaIII));
        ResponseStatusException exceptionIV = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placaIV));

        assertEquals(BAD_REQUEST, exceptionI.getStatusCode());
        assertEquals("Formato de placa incorreto.", exceptionI.getReason());
        assertEquals(BAD_REQUEST, exceptionII.getStatusCode());
        assertEquals("Formato de placa incorreto.", exceptionII.getReason());
        assertEquals(BAD_REQUEST, exceptionIII.getStatusCode());
        assertEquals("Formato de placa incorreto.", exceptionIII.getReason());
        assertEquals(BAD_REQUEST, exceptionIV.getStatusCode());
        assertEquals("Formato de placa incorreto.", exceptionIV.getReason());
    }
}
