package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao.PREVENTIVA;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class ValidaTipoManutencaoValidatorTest {

    @InjectMocks
    private ValidaTipoManutencaoValidator tested;

    @Test
    @DisplayName("Não deve dar erro se tipo de manutencao for valido")
    void naoDeveDarErroSeTipoManutencaoValido(){

        String tipo = PREVENTIVA.getDescricao();

        assertDoesNotThrow(() -> tested.tipoValido(tipo));
    }

    @Test
    @DisplayName("Deve dar erro se tipo de manutencao for nulo")
    void deveDarErroSeTipoManutencaoNulo(){

        String tipo = null;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Campo tipo é obrigatório.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se tipo de manutencao for vazio")
    void deveDarErroSeTipoManutencaoVazio(){

        String tipo = "                   ";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Campo tipo é obrigatório.", exception.getReason());
    }

    @Test
    @DisplayName("Deve dar erro se tipo de manutencao for invalido")
    void deveDarErroSeTipoManutencaoInvalido(){

        String tipo = "tipoinvalido";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoValido(tipo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Tipo de manutenção inválido. Tipos aceitos: Preventiva, Corretiva, Preditiva.", exception.getReason());
    }
}
