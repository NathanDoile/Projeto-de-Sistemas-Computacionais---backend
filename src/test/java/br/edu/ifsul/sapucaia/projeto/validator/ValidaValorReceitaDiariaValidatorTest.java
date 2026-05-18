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
public class ValidaValorReceitaDiariaValidatorTest {

    @InjectMocks
    private ValidaValorReceitaDiariaValidator tested;

    @Test
    @DisplayName("Deve dar erro se valor da receita for menor que zero")
    void deveDarErroSeValorReceitaMenorQueZero(){

        Double valor = -5.00;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.isPositivo(valor));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Valor da sua receita deve ser maior que zero.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se valor da receita for igual a zero")
    void deveDarErroSeValorReceitaIgualAZero(){

        Double valor = 0.00;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.isPositivo(valor));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Valor da sua receita deve ser maior que zero.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se valor da receita for maior que zero")
    void naoDeveDarErroSeValorReceitaMaiorQueZero(){

        Double valor = 5.00;

        assertDoesNotThrow(() -> tested.isPositivo(valor));
    }
}
