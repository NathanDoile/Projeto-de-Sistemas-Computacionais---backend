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
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ValidaVeiculoServiceTest {

    @InjectMocks
    private ValidaVeiculoService tested;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Não deve dar erro se placa não existir")
    void naoDeveDarErroPlacaNaoExistir(){

        String placa = "ABC1234";

        when(veiculoRepository.existsByPlacaAndIsAtivo(placa, true))
                .thenReturn(false);

        assertDoesNotThrow(() -> tested.jaExistePlaca(placa));
    }

    @Test
    @DisplayName("Deve dar erro se placa já existir")
    void deveDarErroPlacaJaExistir(){

        String placa = "ABC1234";

        when(veiculoRepository.existsByPlacaAndIsAtivo(placa, true))
                .thenReturn(true);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> tested.jaExistePlaca(placa));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Placa já cadastrada.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se veículo estiver ativo")
    void naoDeveDarErroVeiculoAtivo(){

        Long id = 1L;

        when(veiculoRepository.existsByIdVeiculoAndIsAtivo(id, true))
                .thenReturn(true);

        assertDoesNotThrow(() -> tested.estaAtivo(id));
    }

    @Test
    @DisplayName("Deve dar erro se veículo não estiver ativo")
    void deveDarErroVeiculoNaoAtivo(){

        Long id = 1L;

        when(veiculoRepository.existsByIdVeiculoAndIsAtivo(id, true))
                .thenReturn(false);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> tested.estaAtivo(id));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Veículo não está ativo.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se id existir")
    void naoDeveDarErroIdExistir(){

        Long id = 1L;

        when(veiculoRepository.existsByIdVeiculoAndIsAtivo(id, true))
                .thenReturn(true);

        assertDoesNotThrow(() -> tested.porId(id));
    }

    @Test
    @DisplayName("Deve dar erro se id não existir")
    void deveDarErroIdNaoExistir(){

        Long id = 1L;

        when(veiculoRepository.existsByIdVeiculoAndIsAtivo(id, true))
                .thenReturn(false);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> tested.porId(id));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("ID do veículo não existe.", exception.getReason());
    }


    @Test
    @DisplayName("Não deve dar erro se usuário tiver um veículo")
    void naoDeveDarErroUsuarioComVeiculo(){

        Long idUsuario = 1L;

        when(veiculoRepository.existsByUsuarioIdUsuarioAndIsAtivo(idUsuario, true))
                .thenReturn(true);

        assertDoesNotThrow(() -> tested.porIdUsuario(idUsuario));
    }

    @Test
    @DisplayName("Deve dar erro se usuário não tiver veículo")
    void deveDarErroUsuarioSemVeiculo(){

        Long idUsuario = 1L;

        when(veiculoRepository.existsByUsuarioIdUsuarioAndIsAtivo(idUsuario, true))
                .thenReturn(false);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> tested.porIdUsuario(idUsuario));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário sem veículo.", exception.getReason());
    }
}