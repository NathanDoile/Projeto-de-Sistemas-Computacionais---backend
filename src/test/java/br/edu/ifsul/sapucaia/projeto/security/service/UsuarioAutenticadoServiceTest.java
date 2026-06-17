package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.controller.response.LoginUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticadoServiceTest {

    @InjectMocks
    private UsuarioAutenticadoService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic;

    @BeforeEach
    void setUp() {
        securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMockedStatic.close();
    }

    @Test
    @DisplayName("Deve retornar o usuario autenticado")
    void deveRetornarUsuarioAutenticado(){

        UsuarioSecurity usuarioSecurityMock = mock(UsuarioSecurity.class);

        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuarioSecurityMock);

        UsuarioSecurity response = tested.getUser();

        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();

        assertEquals(usuarioSecurityMock, response);
    }

    @Test
    @DisplayName("Deve retornar usuario response")
    void deveRetornarUsuarioResponse(){

        Long id = 1L;

        UsuarioSecurity usuarioSecurityMock = mock(UsuarioSecurity.class);

        Usuario usuario = usuario();

        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

        when(usuarioSecurityMock.getId()).thenReturn(id);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuarioSecurityMock);
        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(of(usuario));

        LoginUsuarioResponse response = tested.getResponse();

        verify(usuarioSecurityMock).getId();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);

        assertNotNull(response);
        assertEquals(id, response.getIdUsuario());
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(usuario.getDataCadastro(), response.getDataCadastro());
        assertEquals(usuario.getTelefone(), response.getTelefone());
        assertEquals(usuario.isNotificacaoManutencao(), response.isNotificacaoManutencao());
        assertEquals(usuario.isNotificacaoVencimento(), response.isNotificacaoVencimento());
    }

    @Test
    @DisplayName("Deve retornar nulo se usuario não estiver autenticado")
    void deveRetornarNuloSeUsuarioNaoAutenticado(){

        UsuarioSecurity usuarioSecurityMock = mock(UsuarioSecurity.class);

        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(null);

        LoginUsuarioResponse response = tested.getResponse();

        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(usuarioSecurityMock, never()).getId();
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));

        assertNull(response);
    }
}
