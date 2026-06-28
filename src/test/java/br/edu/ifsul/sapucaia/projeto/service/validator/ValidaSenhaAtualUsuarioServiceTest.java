package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve dar erro se a senha estiver incorreta")
    void deveDarErroSeSenhaIncorreta(){

        Usuario usuario = usuario();
        String senhaAtual = "senhaerrada";
        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaAtual, usuario.getSenha())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> tested.validaSenhaAtualUsuario(senhaAtual, id));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail ou senha inválido", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se a senha estiver correta")
    void naoDeveDarErroSeSenhaCorreta(){

        Usuario usuario = usuario();
        String senhaAtual = usuario.getSenha();
        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaAtual, usuario.getSenha())).thenReturn(true);

        assertDoesNotThrow(() -> tested.validaSenhaAtualUsuario(senhaAtual, id));
    }

    @Test
    @DisplayName("Deve dar erro se o usuário não for encontrado")
    void deveDarErroSeUsuarioNaoExistir(){

        String senhaAtual = "qualquerSenha";
        Long id = 999L;

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> tested.validaSenhaAtualUsuario(senhaAtual, id));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail ou senha inválido", exception.getReason());
    }
}