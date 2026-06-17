package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.ValidarCodigoRequest;
import br.edu.ifsul.sapucaia.projeto.security.service.validator.ValidaCodigoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.usuarioComCodigo;
import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.validarCodigoRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarCodigoServiceTest {


    @InjectMocks
    private ValidarCodigoService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaCodigoValidator validaCodigoValidator;

    @Test
    @DisplayName("Deve validar o código com sucesso quando o e-mail existir")
    void deveValidarCodigoComSucesso() {
        ValidarCodigoRequest request = validarCodigoRequest();
        Usuario usuario = usuarioComCodigo(request.getCodigo(), 0);

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.of(usuario));

        tested.validar(request);

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(validaCodigoValidator).validarCodigo(usuario, request.getCodigo());
    }

    @Test
    @DisplayName("Deve disparar NOT_FOUND quando o e-mail do usuário não for encontrado")
    void deveDispararNotFoundQuandoEmailNaoExistir() {
        ValidarCodigoRequest request = validarCodigoRequest();

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.validar(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("E-mail não encontrado.", exception.getReason());

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(validaCodigoValidator, never()).validarCodigo(any(Usuario.class), anyString());
    }
}