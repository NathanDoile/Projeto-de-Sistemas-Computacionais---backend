package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaNovaSenhaUsuarioServiceTest {

    @InjectMocks
    private ValidaNovaSenhaUsuarioService tested;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve dar erro se as senhas forem iguais")
    void deveDarErroSeSenhasIguais(){

        Usuario usuario = usuario();
        String novaSenha = "senhaIgual";

        when(passwordEncoder.matches(novaSenha, usuario.getSenha())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> tested.validaIgualdadeEntreSenhas(usuario, novaSenha));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("A nova senha precisa ser diferente da atual.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se as senhas forem diferentes")
    void naoDeveDarErroSeSenhasDiferentes(){

        Usuario usuario = usuario();
        String novaSenha = "senhadiferente";

        when(passwordEncoder.matches(novaSenha, usuario.getSenha())).thenReturn(false);

        assertDoesNotThrow(() -> tested.validaIgualdadeEntreSenhas(usuario, novaSenha));

    }
}