package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarNotificacaoManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.alterarNotificacaoManutencaoRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class AlterarNotificacaoManutencaoServiceTest {

    @InjectMocks
    private AlterarNotificacaoManutencaoService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve alterar a notificação de manutenção corretamente")
    void deveAlterarNotificacaoManutencaoCorretamente() {

        AlterarNotificacaoManutencaoRequest request = alterarNotificacaoManutencaoRequest();
        Usuario usuario = usuario();
        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacaoManutencao(id, request);

        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(id, response.getIdUsuario());
        assertFalse(response.isNotificacaoManutencao());
        assertTrue(response.isAtivo());
    }

    @Test
    @DisplayName("Não deve alterar notificação se usuário não existir")
    void naoDeveAlterarNotificacaoSeUsuarioNaoExistir() {

        AlterarNotificacaoManutencaoRequest request = alterarNotificacaoManutencaoRequest();
        Long id = 1L;

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.alterarNotificacaoManutencao(id, request));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado.", exception.getReason());

        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(usuarioRepository, never()).save(any());
    }
}
