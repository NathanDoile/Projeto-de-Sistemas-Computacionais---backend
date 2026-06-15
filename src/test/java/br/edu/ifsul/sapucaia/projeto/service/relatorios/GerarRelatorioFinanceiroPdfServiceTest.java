package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.PeriodoRelatorioFinanceiro;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataRelatorioFinanceiroPdfValidator;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.PeriodoRelatorioFinanceiro.*;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto.MANUTENCAO;
import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.receitaDiaria;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GerarRelatorioFinanceiroPdfServiceTest {
    @InjectMocks
    private GerarRelatorioFinanceiroPdfService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private ValidaDataRelatorioFinanceiroPdfValidator validaDataRelatorioFinanceiroPdfValidator;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Deve gerar o relatório corretamente - semanal")
    void deveGerarRelatorioCorretamenteSemanal(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setCustos(List.of(custo()));
        veiculo.setUsuario(usuario);

        PeriodoRelatorioFinanceiro periodo = SEMANAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.with(DayOfWeek.MONDAY);
        LocalDate dataFim = dataReferencia.with(DayOfWeek.SUNDAY);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of(receitaDiaria()));
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(veiculo.getCustos());

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, periodo);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    @Test
    @DisplayName("Deve gerar o relatório corretamente - mensal")
    void deveGerarRelatorioCorretamenteMensal(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setCustos(List.of(custo()));
        veiculo.setUsuario(usuario);

        PeriodoRelatorioFinanceiro periodo = MENSAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.withDayOfMonth(1);
        LocalDate dataFim = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of(receitaDiaria()));
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(veiculo.getCustos());

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, periodo);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    @Test
    @DisplayName("Deve gerar o relatório corretamente - anual")
    void deveGerarRelatorioCorretamenteAnual(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setCustos(List.of(custo()));
        veiculo.setUsuario(usuario);

        PeriodoRelatorioFinanceiro periodo = ANUAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.withDayOfYear(1);
        LocalDate dataFim = dataReferencia.withDayOfYear(dataReferencia.lengthOfYear());

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of(receitaDiaria()));
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(veiculo.getCustos());

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, periodo);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    @Test
    @DisplayName("Deve gerar o relatório corretamente - custos diferentes")
    void deveGerarRelatorioCorretamenteCustosDiferentes(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setUsuario(usuario);

        Custo custo1 = custo();
        Custo custo2 = custo();
        custo2.setTipo(MANUTENCAO);

        veiculo.setCustos(List.of(custo1, custo2));

        PeriodoRelatorioFinanceiro periodo = SEMANAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.with(DayOfWeek.MONDAY);
        LocalDate dataFim = dataReferencia.with(DayOfWeek.SUNDAY);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of(receitaDiaria()));
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(List.of(custo1, custo2));

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, periodo);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    @Test
    @DisplayName("Deve gerar o relatório, mas sem linhas")
    void deveGerarRelatorioSemLinhas(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setUsuario(usuario);

        PeriodoRelatorioFinanceiro periodo = SEMANAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.with(DayOfWeek.MONDAY);
        LocalDate dataFim = dataReferencia.with(DayOfWeek.SUNDAY);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of());
            when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(List.of());

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, periodo);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    @Test
    @DisplayName("Deve gerar o relatório, mas sem custos")
    void deveGerarRelatorioSemCustos(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setUsuario(usuario);

        PeriodoRelatorioFinanceiro periodo = SEMANAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.with(DayOfWeek.MONDAY);
        LocalDate dataFim = dataReferencia.with(DayOfWeek.SUNDAY);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of(receitaDiaria()));
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(List.of());

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, periodo);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    @Test
    @DisplayName("Deve gerar o relatório, mas sem receita")
    void deveGerarRelatorioSemReceita(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setCustos(List.of(custo()));
        veiculo.setUsuario(usuario);

        PeriodoRelatorioFinanceiro periodo = SEMANAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.with(DayOfWeek.MONDAY);
        LocalDate dataFim = dataReferencia.with(DayOfWeek.SUNDAY);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of());
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(veiculo.getCustos());

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, periodo);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    @Test
    @DisplayName("Não deve gerar o relatório corretamente - usuário não encontrado")
    void naoDeveGerarRelatorioUsuarioNaoEncontrado(){

        Usuario usuario = usuario();

        Long idUsuario = usuario.getIdUsuario();

        PeriodoRelatorioFinanceiro periodo = MENSAL;

        LocalDate dataReferencia = DateNow.now();

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(idUsuario);

        assertThrows(ResponseStatusException.class, () -> tested.gerarRelatorioFinanceiro(idUsuario, dataReferencia, periodo));

        verify(validaUsuarioService).porId(idUsuario);
        verify(validaDataRelatorioFinanceiroPdfValidator, never()).naoMaiorQueHoje(any(LocalDate.class));
        verify(veiculoRepository, never()).findByUsuarioIdUsuarioAndIsAtivo(any(Long.class), anyBoolean());
        verify(receitaDiariaRepository, never()).findByUsuarioIdUsuarioAndDataReceitaBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository, never()).findByVeiculoIdVeiculoAndDataPagamentoBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Não deve gerar o relatório corretamente - data maior que hoje")
    void naoDeveGerarRelatorioUsuarioDataMaiorQueHoje(){

        Usuario usuario = usuario();

        Long idUsuario = usuario.getIdUsuario();

        PeriodoRelatorioFinanceiro periodo = MENSAL;

        LocalDate dataReferencia = DateNow.now().plusDays(1);

        doThrow(ResponseStatusException.class).when(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);

        assertThrows(ResponseStatusException.class, () -> tested.gerarRelatorioFinanceiro(idUsuario, dataReferencia, periodo));

        verify(validaUsuarioService).porId(idUsuario);
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository, never()).findByUsuarioIdUsuarioAndIsAtivo(any(Long.class), anyBoolean());
        verify(receitaDiariaRepository, never()).findByUsuarioIdUsuarioAndDataReceitaBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository, never()).findByVeiculoIdVeiculoAndDataPagamentoBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Não deve gerar o relatório corretamente - falha")
    void naoDeveGerarRelatorioCorretamenteFalha(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setCustos(List.of(custo()));
        veiculo.setUsuario(usuario);
        Long idUsuario = usuario.getIdUsuario();

        PeriodoRelatorioFinanceiro periodo = SEMANAL;

        LocalDate dataReferencia = DateNow.now();

        LocalDate dataInicio = dataReferencia.with(DayOfWeek.MONDAY);
        LocalDate dataFim = dataReferencia.with(DayOfWeek.SUNDAY);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of(receitaDiaria()));
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim)).thenReturn(veiculo.getCustos());

        try (MockedStatic<JasperFillManager> jasperMock = mockStatic(JasperFillManager.class)) {
            jasperMock.when(() -> JasperFillManager.fillReport(
                    any(InputStream.class),
                    anyMap(),
                    any(JREmptyDataSource.class))
            ).thenThrow(new JRException("Simula erro interno do JasperReports"));

            assertThrows(ResponseStatusException.class, () -> tested.gerarRelatorioFinanceiro(idUsuario, dataReferencia, periodo));
        }
        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);
    }
}
