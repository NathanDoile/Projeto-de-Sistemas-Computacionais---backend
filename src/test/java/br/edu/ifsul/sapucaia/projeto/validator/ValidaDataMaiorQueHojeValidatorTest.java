package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaDataMaiorQueHojeValidatorTest {

    @InjectMocks
    private ValidaDataMaiorQueHojeValidator tested;

    @Test
    @DisplayName("Deve estourar erro se data maior que hoje")
    void deveEstourarErroSeDataMaiorHoje(){

        LocalDate request = DateNow.now().plusDays(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.naoMaiorQueHoje(request));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("A data não deve ser maior que hoje.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve estourar erro se data igual ou menor que hoje")
    void naoDeveEstourarErroSeDataIgualMenorHoje(){

        LocalDate request = DateNow.now().minusDays(1);

        assertDoesNotThrow(() -> tested.naoMaiorQueHoje(request));
    }
}
