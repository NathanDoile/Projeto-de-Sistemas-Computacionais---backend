package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.ExcluirContaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.excluirContaUsuarioRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class ExcluirContaUsuarioServiceTest {

    @InjectMocks
    private ExcluirContaUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve excluir o usuario corretamente")
    void deveExcluirUsuarioCorretamente(){

        Long id = 1L;

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();

        Usuario usuario = usuario();

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, request.getSenha())).thenReturn(true);
        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));

        tested.excluirConta(id, request);

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository).existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, request.getSenha());
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(request.getSenha(), response.getSenha());
        assertEquals(id, response.getIdUsuario());
        assertFalse(response.isAtivo());
    }

    @Test
    @DisplayName("Não deve excluir o usuario com id errado")
    void naoDeveExcluirUsuarioIdErrado(){

        Long id = 1L;

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.excluirConta(id, request));

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository, never()).existsByIdUsuarioAndSenhaAndIsAtivoTrue(any(Long.class), any(String.class));
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve excluir o usuario com senha errada")
    void naoDeveExcluirUsuarioSenhaErrada(){

        Long id = 1L;

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();
        request.setSenha("Senhaerrada");

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, request.getSenha())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.excluirConta(id, request));

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository).existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, request.getSenha());
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Senha incorreta.", exception.getReason());
    }
}
