package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.ExcluirContaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaCorretaService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirContaUsuarioServiceTest {

    @InjectMocks
    private ExcluirContaUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaSenhaCorretaService validaSenhaCorretaService;

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
    @DisplayName("Deve excluir usuario corretamente")
    void deveExcluirUsuarioCorretamente() {

        mockAuth();

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();
        Usuario usuario = usuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(ID, true))
                .thenReturn(Optional.of(usuario));

        tested.excluirConta(request);

        verify(validaSenhaCorretaService)
                .porIDESenha(ID, request.getSenha());

        verify(usuarioRepository)
                .findByIdUsuarioAndIsAtivo(ID, true);

        verify(usuarioRepository)
                .save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(ID, response.getIdUsuario());
        assertFalse(response.isAtivo());
    }

    @Test
    @DisplayName("Não deve excluir usuario com senha errada")
    void naoDeveExcluirUsuarioSenhaErrada() {

        mockAuth();

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();

        doThrow(ResponseStatusException.class)
                .when(validaSenhaCorretaService)
                .porIDESenha(ID, request.getSenha());

        assertThrows(ResponseStatusException.class,
                () -> tested.excluirConta(request));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve excluir usuario se não existir")
    void naoDeveExcluirUsuarioIdErrado() {

        mockAuth();

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(ID, true))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> tested.excluirConta(request));

        verify(usuarioRepository, never()).save(any());
    }
}