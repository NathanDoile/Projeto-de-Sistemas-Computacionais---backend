package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ValidaVeiculoServiceTest {

    @InjectMocks
    private ValidaVeiculoService tested;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Não deve dar erro se id do veículo existir")
    void naoDeveDarErroIdVeiculoExistir(){

        Long id = 1L;
        boolean isAtivo = true;

        when(veiculoRepository.existsByIdVeiculoAndIsAtivo(id, isAtivo))
                .thenReturn(true);

        assertDoesNotThrow(() -> tested.porId(id));
    }

    @Test
    @DisplayName("Deve dar erro se id do veículo não existir")
    void deveDarErroIdVeiculoNaoExistir(){

        Long id = 1L;

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> tested.porId(id));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("ID do veículo não existe.", exception.getReason());
    }
}