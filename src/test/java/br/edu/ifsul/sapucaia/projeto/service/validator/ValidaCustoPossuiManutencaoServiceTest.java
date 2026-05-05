package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;

@ExtendWith(MockitoExtension.class)
class ValidaCustoPossuiManutencaoServiceTest {

    @InjectMocks
    private ValidaCustoPossuiManutencaoService tested;

    @Mock
    private CustoRepository custoRepository;

    @Test
    @DisplayName("Não deve dar erro se o custo não possui manutenção")
    void naoDeveDarErroSeCustoNaoPossuiManutencao() {

        Long id = 1L;
        Custo custo = Custo.builder()
                .idCusto(id)
                .manutencao(null)
                .build();

        when(custoRepository.findById(id)).thenReturn(Optional.of(custo));

        assertDoesNotThrow(() -> tested.porId(id));
    }

    @Test
    @DisplayName("Deve dar erro se o custo já possui manutenção")
    void deveDarErroSeCustoPossuiManutencao() {

        Long id = 1L;
        Manutencao manutencao = Manutencao.builder().idManutencao(1L).build();
        Custo custo = Custo.builder()
                .idCusto(id)
                .manutencao(manutencao)
                .build();

        when(custoRepository.findById(id)).thenReturn(Optional.of(custo));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tested.porId(id));

        assertEquals(CONFLICT, exception.getStatusCode());
        assertEquals("Custo já possui uma manutenção.", exception.getReason());
    }
}
