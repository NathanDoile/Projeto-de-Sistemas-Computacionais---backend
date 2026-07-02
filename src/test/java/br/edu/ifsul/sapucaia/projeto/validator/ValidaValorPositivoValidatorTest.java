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
class ValidaValorPositivoValidatorTest {

    @InjectMocks
    private ValidaValorPositivoValidator tested;

    @Test
    @DisplayName("Deve retornar erro se valor menor ou igual a zero")
    void deveRetornarErroSeValorMenorIgualZero(){

        Double valor = -524.32;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.isPositivo(valor));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Valor deve ser maior que zero.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve retornar erro se valor maior que zero")
    void naoDeveRetornarErroSeValorMaiorZero(){

        Double valor = 5.0;

        assertDoesNotThrow(() -> tested.isPositivo(valor));
    }
}
