package br.edu.ifsul.sapucaia.projeto.security.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
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

import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.usuarioComCodigo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidaCodigoValidatorTest {

    @InjectMocks
    private ValidaCodigoValidator tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve passar na validação normalmente quando o código for igual")
    void deveValidarCodigoComSucesso() {
        Usuario usuario = usuarioComCodigo("12345678", 0);

        tested.validarCodigo(usuario, "12345678");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve disparar BAD_REQUEST se o usuário não possuir código gerado")
    void deveDispararBadRequestQuandoUsuarioNaoTiverCodigoGerado() {
        Usuario usuario = usuarioComCodigo(null, 0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> tested.validarCodigo(usuario, "123456"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ValidaCodigoValidator.MSG_SEM_CODIGO, exception.getReason());

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve disparar BAD_REQUEST, aumentar tentativas e salvar quando o código for incorreto")
    void deveDispararBadRequestEAumentarTentativasQuandoCodigoForIncorreto() {
        Usuario usuario = usuarioComCodigo("12345678", 2);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> tested.validarCodigo(usuario, "87654321"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ValidaCodigoValidator.MSG_CODIGO_INVALIDO, exception.getReason());

        verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario salvo = usuarioCaptor.getValue();
        
        assertEquals(3, salvo.getTentativasRedefinirSenha());
    }
}