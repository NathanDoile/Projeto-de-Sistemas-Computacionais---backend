package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.RetornaDadosVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetornaDadosVeiculoServiceTest {

    @InjectMocks
    private RetornaDadosVeiculoService tested;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Retornar os dados do veículo")
    void retornaDadosVeiculoPossuiVeiculo(){

        Veiculo veiculo = veiculo();

        Long id = veiculo.getIdVeiculo();

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(id, true)).thenReturn(veiculo);

        RetornaDadosVeiculoResponse response = tested.dadosVeiculo(id);

        verify(validaVeiculoService).porId(id);
        verify(validaVeiculoService).estaAtivo(id);
        verify(veiculoRepository).findByIdVeiculoAndIsAtivo(id, true);

        assertEquals(id, response.getIdVeiculo());
        assertEquals(veiculo.getModelo(), response.getModelo());
        assertEquals(veiculo.getDataUltimaAtualizacaoKm(), response.getDataUltimaAtualizacaoKm());
        assertEquals(veiculo.getMarca(), response.getMarca());
        assertEquals(veiculo.getPlaca(), response.getPlaca());
        assertEquals(veiculo.getTipo().toString(), response.getTipo());
        assertEquals(veiculo.getAno(), response.getAno());
        assertEquals(veiculo.getCor(), response.getCor());
        assertEquals(veiculo.getKmAtual(), response.getKmAtual());
        assertEquals(veiculo.getIntervaloEntreManutencoesKm(), response.getIntervaloEntreManutencoesKm());
        assertEquals(veiculo.getIntervaloEntreManutencoesMeses(), response.getIntervaloEntreManutencoesMeses());
        assertEquals(veiculo.getProximaManutencaoKm(), response.getProximaManutencaoKm());
        assertEquals(veiculo.getProximaManutencaoData(), response.getProximaManutencaoData());
    }

    @Test
    @DisplayName("Não retornar os dados do veículo, pois veículo não foi encontrado")
    void naoretornaDadosVeiculo(){

        Long id = 1L;

        doThrow(ResponseStatusException.class).when(validaVeiculoService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.dadosVeiculo(id));

        verify(validaVeiculoService).porId(id);
        verify(validaVeiculoService, never()).estaAtivo(any(Long.class));
        verify(veiculoRepository, never()).findByIdVeiculoAndIsAtivo(any(Long.class), any(Boolean.class));
    }

    @Test
    @DisplayName("Não retornar os dados do veículo, pois veículo está inativo")
    void naoretornaDadosVeiculoInativo(){

        Long id = 1L;
        doThrow(ResponseStatusException.class).when(validaVeiculoService).estaAtivo(id);

        assertThrows(ResponseStatusException.class, () -> tested.dadosVeiculo(id));

        verify(validaVeiculoService).porId(id);
        verify(validaVeiculoService).estaAtivo(id);
        verify(veiculoRepository, never()).findByIdVeiculoAndIsAtivo(any(Long.class), any(Boolean.class));
    }
}
