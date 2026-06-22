package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
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

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Deve gerar o relatório corretamente")
    void deveGerarRelatorioCorretamente() {

        String tipoPeriodo = "ano";
        String dataReferencia = DateNow.now().toString();

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Veiculo veiculo = new Veiculo();
        veiculo.setIdVeiculo(1L);

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(
                usuarioSecurity.getId(), true))
                .thenReturn(veiculo);

        PeriodoData periodoData = new PeriodoData(
                DateNow.now().with(TemporalAdjusters.firstDayOfYear()),
                DateNow.now().with(TemporalAdjusters.lastDayOfYear())
        );

        Manutencao manutencao = manutencao();
        manutencao.setCusto(custo());

        List<Manutencao> manutencoes = of(manutencao, manutencao);

        when(periodoDataHelper.calcularData(tipoPeriodo, dataReferencia))
                .thenReturn(periodoData);

        when(manutencaoRepository
                .findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween(
                        1L,
                        true,
                        periodoData.dataInicio(),
                        periodoData.dataFim()))
                .thenReturn(manutencoes);

        try {

            byte[] response = tested.exportar(tipoPeriodo, dataReferencia);

            verify(validaVeiculoService).porIdUsuario(usuarioSecurity.getId());
            verify(validaTipoPeriodoValidator).porTipo(tipoPeriodo);
            verify(periodoDataHelper).calcularData(tipoPeriodo, dataReferencia);

            verify(manutencaoRepository)
                    .findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween(
                            1L,
                            true,
                            periodoData.dataInicio(),
                            periodoData.dataFim());

            assertNotNull(response);
            assertTrue(response.length > 0);

        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Não deve gerar o relatório corretamente")
    void naoDeveGerarRelatorioCorretamente() {

        String tipoPeriodo = "ano";
        String dataReferencia = DateNow.now().toString();

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        doThrow(ResponseStatusException.class)
                .when(validaVeiculoService)
                .porIdUsuario(usuarioSecurity.getId());

        assertThrows(
                ResponseStatusException.class,
                () -> tested.exportar(tipoPeriodo, dataReferencia)
        );

        verify(validaVeiculoService).porIdUsuario(usuarioSecurity.getId());

        verify(validaTipoPeriodoValidator, never())
                .porTipo(anyString());

        verify(periodoDataHelper, never())
                .calcularData(anyString(), anyString());

        verify(manutencaoRepository, never())
                .findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween(
                        anyLong(),
                        anyBoolean(),
                        any(LocalDate.class),
                        any(LocalDate.class));
    }

    @Test
    @DisplayName("Não deve gerar o relatório corretamente se tipo inválido")
    void naoDeveGerarRelatorioCorretamenteSeTipoInvalido() {

        String tipoPeriodo = "invalido";
        String dataReferencia = DateNow.now().toString();

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        doThrow(ResponseStatusException.class)
                .when(validaTipoPeriodoValidator)
                .porTipo(tipoPeriodo);

        assertThrows(
                ResponseStatusException.class,
                () -> tested.exportar(tipoPeriodo, dataReferencia)
        );

        verify(validaVeiculoService).porIdUsuario(usuarioSecurity.getId());
        verify(validaTipoPeriodoValidator).porTipo(tipoPeriodo);

        verify(periodoDataHelper, never())
                .calcularData(anyString(), anyString());

        verify(manutencaoRepository, never())
                .findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween(
                        anyLong(),
                        anyBoolean(),
                        any(LocalDate.class),
                        any(LocalDate.class));
    }
}