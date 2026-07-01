package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaAnoVeiculoValidatorTest {

    @InjectMocks
    private ValidaAnoVeiculoValidator tested;

    @Test
    @DisplayName("Não deve dar erro se ano do veículo for válido")
    void naoDeveDarErroSeAnoVeiculoValido(){

        int ano = DateNow.now().getYear()+1;

        assertDoesNotThrow(() -> tested.anoMenorQueAtual(ano));
    }

    @Test
    @DisplayName("Deve dar erro se ano do veículo for inválido")
    void deveDarErroSeAnoVeiculoInvalido(){

        int ano = DateNow.now().getYear() + 5;

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> tested.anoMenorQueAtual(ano));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals(("Ano do veículo não pode ser maior que " + (DateNow.now().getYear() + 1)), exception.getReason());
    }
}