package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.InformacoesManutencaoVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InformacoesManutencaoVeiculoServiceTest {

    @InjectMocks
    private InformacoesManutencaoVeiculoService tested;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Deve retornar as informações de manutenção do veículo corretamente")
    void deveRetornarInformacoesManutencaoVeiculo() {

        Veiculo veiculo = veiculo();
        veiculo.setManutencoes(List.of());
        veiculo.setCustos(List.of());

        Custo custoPreventiva = Custo.builder()
                .idCusto(2L)
                .valor(150.0)
                .isAtivo(true)
                .build();

        Custo custoCorretiva = Custo.builder()
                .idCusto(3L)
                .valor(350.0)
                .isAtivo(true)
                .build();

        Custo custoPreditiva = Custo.builder()
                .idCusto(4L)
                .valor(60.0)
                .isAtivo(true)
                .build();

        Manutencao manutencaoPreventiva = Manutencao.builder()
                .idManutencao(1L)
                .tipo(TipoManutencao.PREVENTIVA)
                .dataManutencao(LocalDate.now().minusDays(10))
                .descricao("Troca de óleo")
                .isAtivo(true)
                .custo(custoPreventiva)
                .build();

        Manutencao manutencaoCorretiva = Manutencao.builder()
                .idManutencao(2L)
                .tipo(TipoManutencao.CORRETIVA)
                .dataManutencao(LocalDate.now().minusDays(5))
                .descricao("Reparo de freio")
                .isAtivo(true)
                .custo(custoCorretiva)
                .build();

        Manutencao manutencaoPreditiva = Manutencao.builder()
                .idManutencao(3L)
                .tipo(TipoManutencao.PREDITIVA)
                .dataManutencao(LocalDate.now().minusDays(2))
                .descricao("Análise de vibração")
                .isAtivo(true)
                .custo(custoPreditiva)
                .build();

        custoPreventiva.setManutencao(manutencaoPreventiva);
        custoCorretiva.setManutencao(manutencaoCorretiva);
        custoPreditiva.setManutencao(manutencaoPreditiva);
        custoPreventiva.setVeiculo(veiculo);
        custoCorretiva.setVeiculo(veiculo);
        custoPreditiva.setVeiculo(veiculo);
        veiculo.setManutencoes(List.of(manutencaoPreventiva, manutencaoCorretiva, manutencaoPreditiva));
        veiculo.setCustos(List.of(custoPreventiva, custoCorretiva, custoPreditiva));

        Long idVeiculo = veiculo.getIdVeiculo();
        when(veiculoRepository.findByIdVeiculoAndIsAtivo(idVeiculo, true)).thenReturn(veiculo);

        InformacoesManutencaoVeiculoResponse response = tested.buscarInformacoesManutencao(idVeiculo);

        verify(validaVeiculoService).porId(idVeiculo);
        verify(validaVeiculoService).estaAtivo(idVeiculo);
        verify(veiculoRepository).findByIdVeiculoAndIsAtivo(idVeiculo, true);

        assertEquals(1, response.getTotalManutencoesPreventivas());
        assertEquals(1, response.getTotalManutencoesCorretivas());
        assertEquals(1, response.getTotalManutencoesPreditivas());
        assertEquals(150.0, response.getValorTotalPreventivas());
        assertEquals(350.0, response.getValorTotalCorretivas());
        assertEquals(60.0, response.getValorTotalPreditivas());
        assertEquals(186.67, response.getMediaPrecoManutencao(), 0.01);
        assertEquals(560.0 / veiculo.getKmAtual(), response.getValorCustoPorKmRodado(), 0.000001);
    }

    @Test
    @DisplayName("Não deve retornar as informações de manutenção se o veículo não existir")
    void naoDeveRetornarSeVeiculoNaoExistir() {

        Long idVeiculo = 1L;
        doThrow(ResponseStatusException.class).when(validaVeiculoService).porId(idVeiculo);

        assertThrows(ResponseStatusException.class, () -> tested.buscarInformacoesManutencao(idVeiculo));

        verify(validaVeiculoService).porId(idVeiculo);
        verify(validaVeiculoService, never()).estaAtivo(anyLong());
        verify(veiculoRepository, never()).findByIdVeiculoAndIsAtivo(anyLong(), anyBoolean());
    }

    @Test
    @DisplayName("Não deve retornar as informações de manutenção se o veículo estiver inativo")
    void naoDeveRetornarSeVeiculoEstiverInativo() {

        Long idVeiculo = 1L;
        doThrow(ResponseStatusException.class).when(validaVeiculoService).estaAtivo(idVeiculo);

        assertThrows(ResponseStatusException.class, () -> tested.buscarInformacoesManutencao(idVeiculo));

        verify(validaVeiculoService).porId(idVeiculo);
        verify(validaVeiculoService).estaAtivo(idVeiculo);
        verify(veiculoRepository, never()).findByIdVeiculoAndIsAtivo(anyLong(), anyBoolean());
    }
}
