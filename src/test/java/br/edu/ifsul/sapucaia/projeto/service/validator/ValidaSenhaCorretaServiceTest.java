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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class ValidaSenhaCorretaServiceTest {

    @InjectMocks
    private ValidaSenhaCorretaService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Não deve dar erro o usuário possuir a senha enviada")
    void naoDeveDarErroSenhaCorreta(){
        Long id = 1L;

        String senha = "senha123";

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, senha)).thenReturn(true);

        assertDoesNotThrow(() -> tested.porIDESenha(id, senha));

        verify(usuarioRepository).existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, senha);
    }

    @Test
    @DisplayName("Deve dar erro se o usuário não possuir a senha enviada")
    void deveDarErroSenhaIncorreta(){
        Long id = 1L;

        String senha = "senha123";

        when(usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, senha)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porIDESenha(id, senha));

        verify(usuarioRepository).existsByIdUsuarioAndSenhaAndIsAtivoTrue(id, senha);

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Senha incorreta.", exception.getReason());
    }
}
