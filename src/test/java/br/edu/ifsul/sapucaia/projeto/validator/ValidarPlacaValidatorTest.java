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
public class ValidarPlacaValidatorTest {

    @InjectMocks
    private ValidarPlacaValidator tested;

    @Test
    @DisplayName("Não deve dar erro se placa estiver correta")
    void naoDeveDarErroSePlacaCorreta(){

        String placa = "ABC1D23";

        assertDoesNotThrow(() -> tested.formatoValido(placa));
    }

    @Test
    @DisplayName("Deve dar erro se um dos 3 primeiros digitos não for letra")
    void deveDarErroSeUmDosTresPrimeirosNaoForLetra(){

        String placa = "A2C1D23";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placa));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Formato de placa incorreto.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se quarto digito não for numero")
    void deveDarErroSeQuartoDigitoNaoNumero(){

        String placa = "ABCED23";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placa));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Formato de placa incorreto.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se quinto digito não for numero ou letra")
    void deveDarErroSeQuintoDigitoNaoNumeroouLetra(){

        String placa = "ABC1#23";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placa));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Formato de placa incorreto.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se dois ultimos digitos nao for numero")
    void deveDarErroSeDoisUltimosNaoNumero(){

        String placa = "ABC1D2E";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.formatoValido(placa));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Formato de placa incorreto.", exception.getReason());
    }
}
