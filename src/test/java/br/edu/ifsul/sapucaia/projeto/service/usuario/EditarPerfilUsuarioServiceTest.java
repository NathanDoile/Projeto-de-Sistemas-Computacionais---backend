package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaTelefoneUsuarioService;
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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarPerfilUsuarioServiceTest {

    @InjectMocks
    private EditarPerfilUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private ValidaEmailUsuarioService validaEmailUsuarioService;

    @Mock
    private ValidaTelefoneUsuarioService validaTelefoneUsuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve editar perfil com os dados corretos")
    void deveEditarPerfilComDadosCorretos(){

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Usuario usuario = usuario();

        when(usuarioRepository.findById(usuarioSecurity.getId())).thenReturn(Optional.of(usuario));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editarPerfilUsuario(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaEmailUsuarioService).validaEmailUnicoParaEdicao(request.getEmail(), usuarioSecurity.getId());
        verify(validaTelefoneUsuarioService).validaTelefoneUnicoParaEdicao(request.getTelefone(), usuarioSecurity.getId());
        verify(usuarioRepository).findById(usuarioSecurity.getId());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getNome(), response.getNome());
        assertEquals(request.getTelefone(), response.getTelefone());
    }

    @Test
    @DisplayName("Deve editar perfil com apenas telefone")
    void deveEditarPerfilComApenasTelefone(){

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        request.setEmail(null);
        request.setNome(null);

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Usuario usuario = usuario();

        when(usuarioRepository.findById(usuarioSecurity.getId())).thenReturn(Optional.of(usuario));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editarPerfilUsuario(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaEmailUsuarioService, never()).validaEmailUnicoParaEdicao(request.getEmail(), usuarioSecurity.getId());
        verify(validaTelefoneUsuarioService).validaTelefoneUnicoParaEdicao(request.getTelefone(), usuarioSecurity.getId());
        verify(usuarioRepository).findById(usuarioSecurity.getId());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(request.getTelefone(), response.getTelefone());
    }

    @Test
    @DisplayName("Deve editar perfil com apenas Email")
    void deveEditarPerfilComApenasEmail(){

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        request.setTelefone(null);
        request.setNome(null);

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Usuario usuario = usuario();

        when(usuarioRepository.findById(usuarioSecurity.getId())).thenReturn(Optional.of(usuario));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editarPerfilUsuario(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaEmailUsuarioService).validaEmailUnicoParaEdicao(request.getEmail(), usuarioSecurity.getId());
        verify(validaTelefoneUsuarioService, never()).validaTelefoneUnicoParaEdicao(request.getTelefone(), usuarioSecurity.getId());
        verify(usuarioRepository).findById(usuarioSecurity.getId());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getTelefone(), response.getTelefone());
    }

    @Test
    @DisplayName("Deve editar perfil com apenas nome")
    void deveEditarPerfilComApenasNome(){

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        request.setTelefone(null);
        request.setEmail(null);

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Usuario usuario = usuario();

        when(usuarioRepository.findById(usuarioSecurity.getId())).thenReturn(Optional.of(usuario));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editarPerfilUsuario(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaEmailUsuarioService, never()).validaEmailUnicoParaEdicao(request.getEmail(), usuarioSecurity.getId());
        verify(validaTelefoneUsuarioService, never()).validaTelefoneUnicoParaEdicao(request.getTelefone(), usuarioSecurity.getId());
        verify(usuarioRepository).findById(usuarioSecurity.getId());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(request.getNome(), response.getNome());
        assertEquals(usuario.getTelefone(), response.getTelefone());
    }

    @Test
    @DisplayName("Nao deve editar perfil com o email errado")
    void naoDeveEditarPerfilComEmailErrado(){

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(usuarioRepository.findById(usuarioSecurity.getId())).thenReturn(Optional.of(usuario()));
        doThrow(ResponseStatusException.class).when(validaEmailUsuarioService).validaEmailUnicoParaEdicao(request.getEmail(), usuarioSecurity.getId());

        assertThrows(ResponseStatusException.class, () -> tested.editarPerfilUsuario(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaEmailUsuarioService).validaEmailUnicoParaEdicao(request.getEmail(), usuarioSecurity.getId());
        verify(validaTelefoneUsuarioService, never()).validaTelefoneUnicoParaEdicao(any(String.class), any(Long.class));
        verify(usuarioRepository).findById(usuarioSecurity.getId());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Nao deve editar perfil com o telefone errado")
    void naoDeveEditarPerfilComTelefoneErrado(){

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(usuarioRepository.findById(usuarioSecurity.getId())).thenReturn(Optional.of(usuario()));
        doThrow(ResponseStatusException.class).when(validaTelefoneUsuarioService).validaTelefoneUnicoParaEdicao(request.getTelefone(), usuarioSecurity.getId());

        assertThrows(ResponseStatusException.class, () -> tested.editarPerfilUsuario(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaEmailUsuarioService).validaEmailUnicoParaEdicao(request.getEmail(), usuarioSecurity.getId());
        verify(validaTelefoneUsuarioService).validaTelefoneUnicoParaEdicao(request.getTelefone(), usuarioSecurity.getId());
        verify(usuarioRepository).findById(usuarioSecurity.getId());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}