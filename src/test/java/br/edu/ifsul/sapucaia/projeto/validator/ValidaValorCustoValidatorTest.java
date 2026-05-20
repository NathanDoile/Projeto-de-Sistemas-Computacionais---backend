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
class ValidaValorCustoValidatorTest {

    @InjectMocks
    private ValidaValorCustoValidator tested;

    @Test
    @DisplayName("Deve dar erro se o valor do custo for menor que zero")
    void deveDarErroSeValorCustoMenorQueZero() {

        Double valor = -10.00;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.isPositivo(valor));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Valor do seu custo deve ser maior que zero.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se o valor do custo for igual a zero")
    void deveDarErroSeValorCustoIgualAZero() {

        Double valor = 0.00;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.isPositivo(valor));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Valor do seu custo deve ser maior que zero.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se o valor do custo for maior que zero")
    void naoDeveDarErroSeValorCustoMaiorQueZero() {

        Double valor = 10.00;

        assertDoesNotThrow(() -> tested.isPositivo(valor));
    }
}
