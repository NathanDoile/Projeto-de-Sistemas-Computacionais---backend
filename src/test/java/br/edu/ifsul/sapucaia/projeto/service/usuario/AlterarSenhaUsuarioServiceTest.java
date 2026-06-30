package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarSenhaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaUsuarioServiceTest {

    @InjectMocks
    private AlterarSenhaUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    @Mock
    private ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve alterar a senha corretamente")
    void deveAlterarSenhaCorretamente(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();
        UsuarioSecurity usuarioLogado = usuarioSecurity();
        Usuario usuario = usuario();
        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(request.getNovaSenha())).thenReturn(request.getNovaSenha());

        tested.alterarSenhaUsuario(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(usuario, request.getNovaSenha());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(request.getNovaSenha(), response.getSenha());
        assertEquals(usuario.getIdUsuario(), response.getIdUsuario());
        assertTrue(response.isAtivo());
    }

    @Test
    @DisplayName("Não deve alterar senha se a senha atual está errada")
    void naoDeveAlterarSenhaSeSenhaAtualErrada(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();
        UsuarioSecurity usuarioLogado = usuarioSecurity();
        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        doThrow(ResponseStatusException.class).when(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);

        assertThrows(ResponseStatusException.class, () -> tested.alterarSenhaUsuario(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService, never()).validaIgualdadeEntreSenhas(any(Usuario.class), anyString());
        verify(usuarioRepository, never()).findByIdUsuario(anyLong());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve alterar senha se a senha nova é igual a antiga")
    void naoDeveAlterarSenhaSeSenhaNovaIgualAntiga(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();
        UsuarioSecurity usuarioLogado = usuarioSecurity();
        Usuario usuario = usuario();
        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.of(usuario));
        doThrow(ResponseStatusException.class).when(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(usuario, request.getNovaSenha());

        assertThrows(ResponseStatusException.class, () -> tested.alterarSenhaUsuario(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(usuario, request.getNovaSenha());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve alterar senha se id do Usuario não existir")
    void naoDeveAlterarSenhaSeIdInvalido(){

        AlterarSenhaUsuarioRequest request = alterarSenhaUsuarioRequest();
        UsuarioSecurity usuarioLogado = usuarioSecurity();
        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tested.alterarSenhaUsuario(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaSenhaAtualUsuarioService).validaSenhaAtualUsuario(request.getSenhaAtual(), id);
        verify(validaNovaSenhaUsuarioService, never()).validaIgualdadeEntreSenhas(any(Usuario.class), anyString());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}