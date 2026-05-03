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
class ValidaSenhaAtualUsuarioServiceTest {

    @InjectMocks
    private ValidaSenhaAtualUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve dar erro se a senha estiver incorreta")
    void deveDarErroSeSenhaIncorreta(){

        String senhaAtual = "senhaerrada";
        Long id = usuario().getIdUsuario();

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivo(id, senhaAtual, true)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.validaSenhaAtualUsuario(senhaAtual, id));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail ou senha inválido", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se a senha estiver correta")
    void naoDeveDarErroSeSenhaCorreta(){

        String senhaAtual = usuario().getSenha();
        Long id = usuario().getIdUsuario();

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivo(id, senhaAtual, true)).thenReturn(true);

        assertDoesNotThrow(() -> tested.validaSenhaAtualUsuario(senhaAtual, id));
    }
}
