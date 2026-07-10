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
class ValidaDatasCustoValidatorTest {

    @InjectMocks
    private ValidaDatasCustoValidator tested;

    @Test
    @DisplayName("Deve dar erro se ambas datas forem null")
    void deveDarErroSeAmbasDatasNull(){

        LocalDate dataVencimento = null;
        LocalDate dataPagamento = null;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.ambasNull(dataVencimento, dataPagamento));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Informe uma das datas: data de vencimento ou data de pagamento.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se uma das datas não for null")
    void naoDeveDarErroSeUmaDataNaoNull(){

        LocalDate dataVencimentoI = null;
        LocalDate dataPagamentoI = DateNow.now();

        LocalDate dataVencimentoII = DateNow.now();
        LocalDate dataPagamentoII = null;

        assertDoesNotThrow(() -> tested.ambasNull(dataVencimentoI, dataPagamentoI));
        assertDoesNotThrow(() -> tested.ambasNull(dataVencimentoII, dataPagamentoII));
    }
}
