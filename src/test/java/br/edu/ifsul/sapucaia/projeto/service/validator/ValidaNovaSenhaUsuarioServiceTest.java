package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class ValidaNovaSenhaUsuarioServiceTest {

    @InjectMocks
    private ValidaNovaSenhaUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve dar erro se as senhas forem iguais")
    void deveDarErroSeSenhasIguais(){

        Long id = usuario().getIdUsuario();

        String novaSenha = usuario().getSenha();

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivo(id, novaSenha, true)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.validaIgualdadeEntreSenhas(id, novaSenha));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("A nova senha precisa ser diferente da atual.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se as senhas forem diferentes")
    void naoDeveDarErroSeSenhasDiferentes(){

        Long id = usuario().getIdUsuario();

        String novaSenha = "senhadiferente";

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivo(id, novaSenha, true)).thenReturn(false);

        assertDoesNotThrow(() -> tested.validaIgualdadeEntreSenhas(id, novaSenha));

    }
}
