package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static br.edu.ifsul.sapucaia.projeto.factory.ManutencaoFactory.manutencao;
import static java.time.LocalDate.now;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportarManutencoesServiceTest {

    @InjectMocks
    private ExportarManutencoesService tested;

    @Mock
    private ManutencaoRepository manutencaoRepository;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private PeriodoDataHelper periodoDataHelper;

    @Mock
    private ValidaTipoPeriodoValidator validaTipoPeriodoValidator;

    @Test
    @DisplayName("Deve gerar o relatório corretamente")
    void deveGerarRelatorioCorretamente(){

        Long idVeiculo = 1L;

        String tipoPeriodo = "ano";

        String dataReferencia = now().toString();

        PeriodoData periodoData = new PeriodoData(now().with(TemporalAdjusters.firstDayOfYear()),
                now().with(TemporalAdjusters.lastDayOfYear()));

        Manutencao manutencao = manutencao();
        manutencao.setCusto(custo());

        List<Manutencao> manutencoes = of(manutencao, manutencao);

        when(periodoDataHelper.calcularData(tipoPeriodo, dataReferencia)).thenReturn(periodoData);
        when(manutencaoRepository.findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween
                (idVeiculo, true, periodoData.dataInicio(), periodoData.dataFim())).thenReturn(manutencoes);

        try {
            byte[] response = tested.exportar(idVeiculo, tipoPeriodo, dataReferencia);

            verify(validaVeiculoService).porId(idVeiculo);
            verify(validaTipoPeriodoValidator).porTipo(tipoPeriodo);
            verify(periodoDataHelper).calcularData(tipoPeriodo, dataReferencia);
            verify(manutencaoRepository).findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween
                    (idVeiculo, true, periodoData.dataInicio(), periodoData.dataFim());

            assertNotNull(response);
            assertTrue(response.length > 0);

        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Não deve gerar o relatório corretamente")
    void naoDeveGerarRelatorioCorretamente(){

        Long idVeiculo = 1L;

        String tipoPeriodo = "ano";

        String dataReferencia = now().toString();

        doThrow(ResponseStatusException.class).when(validaVeiculoService).porId(idVeiculo);

        assertThrows(ResponseStatusException.class, () -> tested.exportar(idVeiculo, tipoPeriodo, dataReferencia));

        verify(validaVeiculoService).porId(idVeiculo);
        verify(validaTipoPeriodoValidator, never()).porTipo(any(String.class));
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(manutencaoRepository, never()).findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween
                (any(Long.class), any(Boolean.class), any(LocalDate.class), any(LocalDate.class));

    }

    @Test
    @DisplayName("Não deve gerar o relatório corretamente se tipo inválido")
    void naoDeveGerarRelatorioCorretamenteSeTipoInvalido(){

        Long idVeiculo = 1L;

        String tipoPeriodo = "invalido";

        String dataReferencia = now().toString();

        doThrow(ResponseStatusException.class).when(validaTipoPeriodoValidator).porTipo(tipoPeriodo);

        assertThrows(ResponseStatusException.class, () -> tested.exportar(idVeiculo, tipoPeriodo, dataReferencia));

        verify(validaVeiculoService).porId(idVeiculo);
        verify(validaTipoPeriodoValidator).porTipo(tipoPeriodo);
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(manutencaoRepository, never()).findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween
                (any(Long.class), any(Boolean.class), any(LocalDate.class), any(LocalDate.class));

    }
}
