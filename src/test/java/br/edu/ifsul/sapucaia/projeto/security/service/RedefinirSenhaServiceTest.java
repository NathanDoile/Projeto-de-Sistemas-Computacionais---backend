package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.RedefinirSenhaRequest;
import br.edu.ifsul.sapucaia.projeto.security.service.validator.ValidaCodigoValidator;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaNovaSenhaUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.redefinirSenhaRequest;
import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.usuarioComCodigo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedefinirSenhaServiceTest {

    @InjectMocks
    private RedefinirSenhaService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaCodigoValidator validaCodigoValidator;

    @Mock
    private ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve redefinir a senha com sucesso, limpando o código e as tentativas")
    void deveRedefinirSenhaComSucesso() {
        RedefinirSenhaRequest request = redefinirSenhaRequest();
        Usuario usuario = usuarioComCodigo(request.getCodigo(), 0);

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.of(usuario));

        tested.redefinirSenha(request);

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(validaCodigoValidator).validarCodigo(usuario, request.getCodigo());
        verify(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(usuario.getIdUsuario(), request.getSenha());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario salvo = usuarioCaptor.getValue();
        assertEquals(request.getSenha(), salvo.getSenha());
        assertNull(salvo.getCodigoRedefinirSenha());
        assertEquals(-1, salvo.getTentativasRedefinirSenha());
    }

    @Test
    @DisplayName("Deve dispara rNOT_FOUND quando o e-mail não for encontrado")
    void deveDispararNotFoundQuandoEmailNaoExistir() {
        RedefinirSenhaRequest request = redefinirSenhaRequest();

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> tested.redefinirSenha(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("E-mail não encontrado.", exception.getReason());

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(validaCodigoValidator, never()).validarCodigo(any(Usuario.class), anyString());
        verify(validaNovaSenhaUsuarioService, never()).validaIgualdadeEntreSenhas(anyLong(), anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve redefinir a senha se o código for inválido")
    void naoDeveRedefinirSenhaQuandoCodigoForInvalido() {
        RedefinirSenhaRequest request = redefinirSenhaRequest();
        Usuario usuario = usuarioComCodigo("654321", 0);

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.of(usuario));
        doThrow(ResponseStatusException.class).when(validaCodigoValidator).validarCodigo(usuario, request.getCodigo());

        assertThrows(ResponseStatusException.class, () -> tested.redefinirSenha(request));

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(validaCodigoValidator).validarCodigo(usuario, request.getCodigo());
        verify(validaNovaSenhaUsuarioService, never()).validaIgualdadeEntreSenhas(anyLong(), anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve redefinir a senha se a nova senha for igual à atual")
    void naoDeveRedefinirSenhaQuandoNovaSenhaForIgualAAtual() {
        RedefinirSenhaRequest request = redefinirSenhaRequest();
        Usuario usuario = usuarioComCodigo(request.getCodigo(), 0);

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.of(usuario));
        doThrow(ResponseStatusException.class).when(validaNovaSenhaUsuarioService)
                .validaIgualdadeEntreSenhas(usuario.getIdUsuario(), request.getSenha());

        assertThrows(ResponseStatusException.class, () -> tested.redefinirSenha(request));

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(validaCodigoValidator).validarCodigo(usuario, request.getCodigo());
        verify(validaNovaSenhaUsuarioService).validaIgualdadeEntreSenhas(usuario.getIdUsuario(), request.getSenha());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}