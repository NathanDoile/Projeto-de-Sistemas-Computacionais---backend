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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaDataRelatorioFinanceiroPdfValidatorTest {
    @InjectMocks
    private ValidaDataRelatorioFinanceiroPdfValidator tested;

    @Test
    @DisplayName("Não deve dar erro se a data de referência for menor ou igual a hoje")
    void naoDeveDarErroSeDataMenorOuIgualHoje() {

        assertDoesNotThrow(() -> tested.naoMaiorQueHoje(DateNow.now()));
        assertDoesNotThrow(() -> tested.naoMaiorQueHoje(DateNow.now().minusDays(1)));
    }

    @Test
    @DisplayName("Deve dar erro se a data de referência for maior que hoje")
    void deveDarErroSeDataMaiorQueHoje() {

        LocalDate data = DateNow.now().plusDays(1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.naoMaiorQueHoje(data));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Data de referência não pode ser superior à hoje", exception.getReason());
    }
}
