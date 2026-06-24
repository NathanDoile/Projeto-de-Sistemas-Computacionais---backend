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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirContaUsuarioServiceTest {

    @InjectMocks
    private ExcluirContaUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private ValidaSenhaCorretaService validaSenhaCorretaService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve excluir o usuario corretamente")
    void deveExcluirUsuarioCorretamente(){

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Usuario usuario = usuario();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(usuarioRepository.findById(usuarioSecurity.getId())).thenReturn(Optional.of(usuario));

        tested.excluirConta(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaSenhaCorretaService).porIDESenha(usuarioSecurity.getId(), request.getSenha());
        verify(usuarioRepository).findById(usuarioSecurity.getId());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(request.getSenha(), response.getSenha());
        assertEquals(usuarioSecurity.getId(), response.getIdUsuario());
        assertFalse(response.isAtivo());
    }

    @Test
    @DisplayName("Não deve excluir o usuario com senha errada")
    void naoDeveExcluirUsuarioSenhaErrada(){

        ExcluirContaUsuarioRequest request = excluirContaUsuarioRequest();
        request.setSenha("Senhaerrada");

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        doThrow(ResponseStatusException.class).when(validaSenhaCorretaService).porIDESenha(usuarioSecurity.getId(), request.getSenha());

        assertThrows(ResponseStatusException.class, () -> tested.excluirConta(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaSenhaCorretaService).porIDESenha(usuarioSecurity.getId(), request.getSenha());
        verify(usuarioRepository, never()).findById(any(Long.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}