package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;

@ExtendWith(MockitoExtension.class)
class ValidaTelefoneUsuarioServiceTest {
    @InjectMocks
    private ValidaTelefoneUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Não deve dar erro se telefone do usuario não for cadastrado ainda")
    void naoDeveDarErroTelefoneNaoCadastrado() {
        String telefone = "51911111111";
        boolean isAtivo = true;

        when(usuarioRepository.existsByTelefone(telefone, isAtivo)).thenReturn(false);

        assertDoesNotThrow(() -> tested.validaTelefoneUnico(telefone));
    }

    @Test
    @DisplayName("Deve dar erro se telefone já está cadastrado")
    void DeveDarErroTelefoneCadastrado(){
        String telefone = "51911111111";
        boolean isAtivo = true;

        when(usuarioRepository.existsByTelefone(telefone, isAtivo)).thenReturn(true);

        ResponseStatusException exception =  assertThrows(ResponseStatusException.class, () -> tested.validaTelefoneUnico(telefone));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Esse telefone já foi cadastrado.", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se telefone do usuario não for cadastrado ainda - edicao")
    void naoDeveDarErroTelefoneNaoCadastradoEdicao(){
        String telefone = "51911111111";
        Long id = 1L;
        boolean isAtivo = true;

        when(usuarioRepository.existsByTelefoneAndIdUsuarioNot(telefone, id, isAtivo)).thenReturn(false);

        assertDoesNotThrow(() -> tested.validaTelefoneUnicoParaEdicao(telefone, id));
    }

    @Test
    @DisplayName("Deve dar erro se telefone já está cadastrado - editar")
    void DeveDarErroTelefoneCadastradoEdicao(){
        String telefone = "51911111111";
        Long id = 1L;
        boolean isAtivo = true;

        when(usuarioRepository.existsByTelefoneAndIdUsuarioNot(telefone, id, isAtivo)).thenReturn(true);

        ResponseStatusException exception =  assertThrows(ResponseStatusException.class, () -> tested.validaTelefoneUnicoParaEdicao(telefone, id));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Esse telefone já foi cadastrado.", exception.getReason());
    }
}
