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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class ValidaSenhaCorretaServiceTest {

    @InjectMocks
    private ValidaSenhaCorretaService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Não deve dar erro o usuário possuir a senha enviada")
    void naoDeveDarErroSenhaCorreta(){
        Usuario usuario = usuario();
        Long id = 1L;
        String senha = "senha123";

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);

        assertDoesNotThrow(() -> tested.porIDESenha(id, senha));

        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(passwordEncoder).matches(senha, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve dar erro se o usuário não possuir a senha enviada")
    void deveDarErroSenhaIncorreta(){
        Usuario usuario = usuario();
        Long id = 1L;
        String senha = "senha123";

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porIDESenha(id, senha));

        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(passwordEncoder).matches(senha, usuario.getSenha());

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Senha incorreta.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se o usuário não for encontrado")
    void deveDarErroSeUsuarioNaoExistir(){
        Long id = 999L;
        String senha = "qualquerSenha";

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porIDESenha(id, senha));

        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Senha incorreta.", exception.getReason());
    }
}