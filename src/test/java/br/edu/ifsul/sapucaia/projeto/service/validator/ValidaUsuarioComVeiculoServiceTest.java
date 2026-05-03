package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class ValidaUsuarioComVeiculoServiceTest {

    @InjectMocks
    private ValidaUsuarioComVeiculoService tested;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Deve dar erro se usuario ja tiver veiculo cadastrado")
    void deveDarErroSeUsuarioTiverVeiculo(){

        Usuario usuario = usuario();

        when(veiculoRepository.existsByUsuario(usuario)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porUsuario(usuario));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Usuário já possui veículo cadastrado", exception.getReason());
    }

    @Test
    @DisplayName("Não deve dar erro se usuario não tiver veiculo cadastrado")
    void naoDeveDarErroSeUsuarioNaoTiverVeiculo(){

        Usuario usuario = usuario();

        when(veiculoRepository.existsByUsuario(usuario)).thenReturn(false);

        assertDoesNotThrow(() -> tested.porUsuario(usuario));
    }
}
