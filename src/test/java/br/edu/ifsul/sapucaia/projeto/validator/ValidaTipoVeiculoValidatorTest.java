package br.edu.ifsul.sapucaia.projeto.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class ValidaTipoVeiculoValidatorTest {

    @InjectMocks
    private ValidaTipoVeiculoValidator tested;

    @Test
    @DisplayName("Não deve dar erro se tipo de veiculo for valido")
    void naoDeveDarErroSeTipoVeiculoValido(){

        String veiculo = "carro";

        assertDoesNotThrow(() -> tested.tipoAceito(veiculo));
    }

    @Test
    @DisplayName("Deve dar erro se tipo de veiculo for invalido")
    void deveDarErroSeTipoVeiculoInvalido(){

        String veiculo = "caminhão";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.tipoAceito(veiculo));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("Tipo de veículo não aceito.", exception.getReason());
    }
}
