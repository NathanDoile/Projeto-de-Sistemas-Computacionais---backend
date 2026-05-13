package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.validator.ValidaFormatoMetaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaFormatoMetaValidatorTest {

    @InjectMocks
    private ValidaFormatoMetaValidator tested;

    @Test
    @DisplayName("Não deve dar erro se o formato da meta for válido")
    void naoDeveDarErroSeFormatoMetaValido() {

        assertDoesNotThrow(() -> tested.formatoValido("Diária"));
        assertDoesNotThrow(() -> tested.formatoValido("Semanal"));
        assertDoesNotThrow(() -> tested.formatoValido("mensal"));
    }

    @Test
    @DisplayName("Deve dar erro se o formato da meta for inválido")
    void deveDarErroSeFormatoMetaInvalido() {

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.formatoValido("anual"));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Formato da meta deve ser diária, semanal ou mensal", exception.getReason());
    }
}
