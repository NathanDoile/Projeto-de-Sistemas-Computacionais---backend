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
public class ValidaKmAtualizadoVeiculoValidatorTest {

    @InjectMocks
    private ValidaKmAtualizadoVeiculoValidator tested;

    @Test
    @DisplayName("Não deve retornar erro se km atualizado maior que km atual")
    void naoDeveRetornarErroSeKmAtualizadoMaiorQueKmAtual(){

        int kmAtualizado = 43500;

        int kmAtual = 42000;

        assertDoesNotThrow(() -> tested.maiorQueAtual(kmAtualizado, kmAtual));
    }

    @Test
    @DisplayName("Deve retornar erro se km atualizado nao maior que km atual")
    void deveRetornarErroSeKmAtualizadoNaoMaiorQueKmAtual(){

        int kmAtualizadoI = 42000;
        int kmAtualI = 43500;

        int kmAtualizadoII = 43500;
        int kmAtualII = 43500;

        ResponseStatusException exceptionI = assertThrows(ResponseStatusException.class,
                () -> tested.maiorQueAtual(kmAtualizadoI, kmAtualI));

        ResponseStatusException exceptionII = assertThrows(ResponseStatusException.class,
                () -> tested.maiorQueAtual(kmAtualizadoII, kmAtualII));

        assertEquals("O quilômetro atualizado deve ser maior que o registrado anteriormente.", exceptionI.getReason());
        assertEquals(BAD_REQUEST, exceptionI.getStatusCode());
        assertEquals("O quilômetro atualizado deve ser maior que o registrado anteriormente.", exceptionII.getReason());
        assertEquals(BAD_REQUEST, exceptionII.getStatusCode());
    }

}
