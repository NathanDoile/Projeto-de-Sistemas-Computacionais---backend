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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.editarPerfilUsuarioRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarPerfilUsuarioServiceTest {

    @InjectMocks
    private EditarPerfilUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaEmailUsuarioService validaEmailUsuarioService;

    @Mock
    private ValidaTelefoneUsuarioService validaTelefoneUsuarioService;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private UsuarioSecurity usuarioSecurity;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    private final Long ID = 1L;

    private void mockAuth() {
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(usuarioSecurity.getId()).thenReturn(ID);
    }

    @Test
    @DisplayName("Deve editar perfil com os dados corretos")
    void deveEditarPerfilComDadosCorretos() {

        mockAuth();

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        Usuario usuario = usuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(ID, true))
                .thenReturn(Optional.of(usuario));

        tested.editarPerfilUsuario(request);

        verify(validaEmailUsuarioService)
                .validaEmailUnicoParaEdicao(request.getEmail(), ID);

        verify(validaTelefoneUsuarioService)
                .validaTelefoneUnicoParaEdicao(request.getTelefone(), ID);

        verify(usuarioRepository).save(usuarioCaptor.capture());
    }

    @Test
    @DisplayName("Deve editar perfil com apenas telefone")
    void deveEditarPerfilComApenasTelefone() {

        mockAuth();

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        request.setEmail(null);
        request.setNome(null);

        Usuario usuario = usuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(ID, true))
                .thenReturn(Optional.of(usuario));

        tested.editarPerfilUsuario(request);

        verify(validaEmailUsuarioService, never())
                .validaEmailUnicoParaEdicao(any(), anyLong());

        verify(validaTelefoneUsuarioService)
                .validaTelefoneUnicoParaEdicao(request.getTelefone(), ID);
    }

    @Test
    @DisplayName("Deve editar perfil com apenas email")
    void deveEditarPerfilComApenasEmail() {

        mockAuth();

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        request.setTelefone(null);
        request.setNome(null);

        Usuario usuario = usuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(ID, true))
                .thenReturn(Optional.of(usuario));

        tested.editarPerfilUsuario(request);

        verify(validaEmailUsuarioService)
                .validaEmailUnicoParaEdicao(request.getEmail(), ID);

        verify(validaTelefoneUsuarioService, never())
                .validaTelefoneUnicoParaEdicao(any(), anyLong());
    }

    @Test
    @DisplayName("Nao deve editar quando email for invalido")
    void naoDeveEditarPerfilComEmailErrado() {

        mockAuth();

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(ID, true))
                .thenReturn(Optional.of(usuario()));

        doThrow(ResponseStatusException.class)
                .when(validaEmailUsuarioService)
                .validaEmailUnicoParaEdicao(request.getEmail(), ID);

        assertThrows(ResponseStatusException.class,
                () -> tested.editarPerfilUsuario(request));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Nao deve editar quando telefone for invalido")
    void naoDeveEditarPerfilComTelefoneErrado() {

        mockAuth();

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(ID, true))
                .thenReturn(Optional.of(usuario()));

        doThrow(ResponseStatusException.class)
                .when(validaTelefoneUsuarioService)
                .validaTelefoneUnicoParaEdicao(request.getTelefone(), ID);

        assertThrows(ResponseStatusException.class,
                () -> tested.editarPerfilUsuario(request));

        verify(usuarioRepository, never()).save(any());
    }
}