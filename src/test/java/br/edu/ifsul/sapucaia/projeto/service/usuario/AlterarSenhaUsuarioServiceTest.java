package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarSenhaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaNovaSenhaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.alterarSenhaUsuarioRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaUsuarioServiceTest {

    @InjectMocks
    private AlterarSenhaUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    @Mock
    private ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve alterar a senha corretamente")
    void deveAlterarSenhaCorretamente(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));

        tested.alterarSenhaUsuario(id, request);

        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(id, request.getNovaSenha());
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(request.getNovaSenha(), response.getSenha());
        assertEquals(id, response.getIdUsuario());
        assertTrue(response.isAtivo());
    }

    @Test
    @DisplayName("Não deve alterar senha se a senha atual está errada")
    void naoDeveAlterarSenhaSeSenhaAtualErrada(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class).when(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);

        assertThrows(ResponseStatusException.class, () -> tested.alterarSenhaUsuario(id, request));

        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService, never()).validaIgualdadeEntreSenhas(any(Long.class), any(String.class));
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve alterar senha se a senha nova é igual a antiga")
    void naoDeveAlterarSenhaSeSenhaNovaIgualAntiga(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class).when(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(id, request.getNovaSenha());

        assertThrows(ResponseStatusException.class, () -> tested.alterarSenhaUsuario(id, request));

        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(id, request.getNovaSenha());
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve alterar senha se id do Usuario não existir")
    void naoDeveAlterarSenhaSeIdInvalido(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class).when(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);

        assertThrows(ResponseStatusException.class, () -> tested.alterarSenhaUsuario(id, request));

        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(id, request.getNovaSenha());
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
