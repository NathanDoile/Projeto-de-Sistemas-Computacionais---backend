package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ValidaCustoServiceTest {

    @InjectMocks
    private ValidaCustoService tested;

    @Mock
    private CustoRepository custoRepository;

    @Test
    @DisplayName("Não deve dar erro se o custo existir")
    void naoDeveDarErroSeCustoExistir() {

        Long id = 1L;

        when(custoRepository.existsByIdCustoAndIsAtivo(id, true)).thenReturn(true);

        assertDoesNotThrow(() -> tested.porId(id));
    }

    @Test
    @DisplayName("Deve dar erro se o custo não existir")
    void deveDarErroSeCustoNaoExistir() {

        Long id = 1L;

        when(custoRepository.existsByIdCustoAndIsAtivo(id, true)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.porId(id));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Id do custo não encontrado.", exception.getReason());
    }
}
