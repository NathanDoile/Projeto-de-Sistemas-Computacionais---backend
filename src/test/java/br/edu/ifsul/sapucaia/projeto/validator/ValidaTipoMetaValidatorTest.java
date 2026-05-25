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
class ValidaTipoMetaValidatorTest {

    @InjectMocks
    private ValidaTipoMetaValidator tested;

    @Test
    @DisplayName("Não deve dar erro se o tipo da meta for válido")
    void naoDeveDarErroSeTipoMetaValido() {

        assertDoesNotThrow(() -> tested.tipoValido("Receita"));
        assertDoesNotThrow(() -> tested.tipoValido("receita"));
        assertDoesNotThrow(() -> tested.tipoValido("Ganho"));
        assertDoesNotThrow(() -> tested.tipoValido("Custo"));
        assertDoesNotThrow(() -> tested.tipoValido("gasto"));
    }

    @Test
    @DisplayName("Deve dar erro se o tipo da meta for inválido")
    void deveDarErroSeTipoMetaInvalido() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> tested.tipoValido("investimento")
        );

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Tipo da meta deve ser receita ou custo", exception.getReason());
    }
}