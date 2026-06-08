package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ValidaUsuarioServiceTest {

    @InjectMocks
    private ValidaUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Não deve dar erro se id do usuário existir")
    void naoDeveDarErroIdUsuarioExistir(){

        Long id = 1L;
        boolean isAtivo = true;

        when(usuarioRepository.existsByIdUsuarioAndIsAtivo(id, isAtivo)).thenReturn(true);

        assertDoesNotThrow(() -> tested.porId(id));
    }

    @Test
    @DisplayName("Deve dar erro se id do usuário não existir")
    void deveDarErroIdUsuarioNaoExistir(){

        Long id = 1L;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porId(id));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("ID do usuário não existe.", exception.getReason());
    }
    @Test
    @DisplayName("Não deve dar erro se e-mail existir")
    void naoDeveDarErroEmailExistir() {

        String email = "teste@email.com";
        boolean isAtivo = true;

        when(usuarioRepository.existsByEmailAndIsAtivo(email, isAtivo)).thenReturn(true);

        assertDoesNotThrow(() -> tested.porEmail(email));
    }

    @Test
    @DisplayName("Deve dar erro se e-mail não existir")
    void deveDarErroEmailNaoExistir() {

        String email = "teste@email.com";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porEmail(email));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("E-mail ou senha inválido", exception.getReason());
    }
}
