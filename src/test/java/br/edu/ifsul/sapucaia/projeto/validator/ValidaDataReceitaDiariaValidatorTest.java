package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataReceitaDiariaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class ValidaDataReceitaDiariaValidatorTest {

    @InjectMocks
    private ValidaDataReceitaDiariaValidator tested;

    @Test
    @DisplayName("Não deve dar erro se a data da receita for menor ou igual a hoje")
    void naoDeveDarErroSeDataMenorOuIgualHoje() {

        assertDoesNotThrow(() -> tested.naoMaiorQueHoje(now()));
        assertDoesNotThrow(() -> tested.naoMaiorQueHoje(now().minusDays(1)));
    }

    @Test
    @DisplayName("Deve dar erro se a data da receita for maior que hoje")
    void deveDarErroSeDataMaiorQueHoje() {

        LocalDate data = now().plusDays(1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.naoMaiorQueHoje(data));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Data da sua receita não pode ser superior à hoje", exception.getReason());
    }
}
