package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.AtualizarKmVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaKmAtualizadoVeiculoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.atualizarKmVeiculoRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarKmVeiculoServiceTest {

    @InjectMocks
    private AtualizarKmVeiculoService tested;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private ValidaKmAtualizadoVeiculoValidator validaKmAtualizadoVeiculoValidator;

    @Captor
    private ArgumentCaptor<Veiculo> veiculoCaptor;

    @Test
    @DisplayName("Deve atualizar km do veículo corretamente")
    void deveAtualizarKmVeiculoCorretamente(){

        AtualizarKmVeiculoRequest request = atualizarKmVeiculoRequest();

        Veiculo veiculo = veiculo();
        veiculo.setUsuario(usuario());

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(request.getIdUsuario(), true)).thenReturn(veiculo);

        tested.atualizar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(request.getIdUsuario(), true);
        verify(validaKmAtualizadoVeiculoValidator).maiorQueAtual(request.getKmAtualizado(), veiculo().getKmAtual());
        verify(veiculoRepository).save(veiculoCaptor.capture());

        Veiculo response = veiculoCaptor.getValue();

        assertEquals(request.getIdUsuario(), response.getUsuario().getIdUsuario());
        assertEquals(request.getKmAtualizado(), response.getKmAtual());
    }

    @Test
    @DisplayName("Não deve atualizar km do veiculo se id do Usuario incorreto")
    void naoDeveAtualizarKmVeiculoSeIdUsuarioIncorreto(){

        AtualizarKmVeiculoRequest request = atualizarKmVeiculoRequest();
        request.setIdUsuario(7L);

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(request.getIdUsuario());

        assertThrows(ResponseStatusException.class, () -> tested.atualizar(request));

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(veiculoRepository, never()).findByUsuarioIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(validaKmAtualizadoVeiculoValidator, never()).maiorQueAtual(any(Integer.class), any(Integer.class));
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Não deve atualizar km do veiculo se km atualizado não maior que o atual")
    void naoDeveAtualizarKmVeiculoSeKmAtualizadoNaoMaiorQueAtual(){

        AtualizarKmVeiculoRequest request = atualizarKmVeiculoRequest();
        request.setKmAtualizado(-47300);

        Veiculo veiculo = veiculo();
        veiculo.setUsuario(usuario());

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(request.getIdUsuario(), true)).thenReturn(veiculo);
        doThrow(ResponseStatusException.class)
                .when(validaKmAtualizadoVeiculoValidator).maiorQueAtual(request.getKmAtualizado(), veiculo.getKmAtual());

        assertThrows(ResponseStatusException.class, () -> tested.atualizar(request));

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(request.getIdUsuario(), true);
        verify(validaKmAtualizadoVeiculoValidator).maiorQueAtual(request.getKmAtualizado(), veiculo.getKmAtual());
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }
}
