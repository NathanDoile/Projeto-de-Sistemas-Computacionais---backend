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
import static org.springframework.http.HttpStatus.CONFLICT;

@ExtendWith(MockitoExtension.class)
public class ValidaEmailUsuarioServiceTest {
    @InjectMocks
    private ValidaEmailUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Não deve dar erro se email do usuario não for cadastrado ainda")
    void naoDeveDarErroEmailNaoCadastrado(){
        String email = "testeEmail@teste.com";
        boolean isAtivo = true;

        when(usuarioRepository.existsByEmailAndIsAtivo(email, isAtivo)).thenReturn(false);

        assertDoesNotThrow(() -> tested.validaEmailUnico(email));
    }

    @Test
    @DisplayName("Deve dar erro se email já está cadastrado")
    void DeveDarErroEmailCadastrado(){
        String email = "testeEmail@teste.com";
        boolean isAtivo = true;

        when(usuarioRepository.existsByEmailAndIsAtivo(email, isAtivo)).thenReturn(true);

        ResponseStatusException exception =  assertThrows(ResponseStatusException.class, () -> tested.validaEmailUnico(email));

       assertEquals(CONFLICT, exception.getStatusCode());
       assertEquals("Esse email já foi cadastrado.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se email do usuario não for cadastrado ainda - edicao")
    void naoDeveDarErroEmailNaoCadastradoEdicao(){
        String email = "testeEmail@teste.com";
        Long id = 1L;
        boolean isAtivo = true;

        when(usuarioRepository.existsByEmailAndIdUsuarioNotAndIsAtivo(email, id, isAtivo)).thenReturn(false);

        assertDoesNotThrow(() -> tested.validaEmailUnicoParaEdicao(email, id));
    }

    @Test
    @DisplayName("Deve dar erro se email já está cadastrado - editar")
    void DeveDarErroEmailCadastradoEdicao(){
        String email = "testeEmail@teste.com";
        Long id = 1L;
        boolean isAtivo = true;

        when(usuarioRepository.existsByEmailAndIdUsuarioNotAndIsAtivo(email, id, isAtivo)).thenReturn(true);

        ResponseStatusException exception =  assertThrows(ResponseStatusException.class, () -> tested.validaEmailUnicoParaEdicao(email, id));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Esse email já foi cadastrado.", exception.getReason());
    }
}
