package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumoFinanceiroPeriodoServiceTest {

    @InjectMocks
    private ResumoFinanceiroPeriodoService tested;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ValidaTipoPeriodoValidator validaTipoPeriodoValidator;

    @Mock
    private PeriodoDataHelper periodoDataHelper;

    @Test
    @DisplayName("Deve retornar os dados corretamente")
    void deveRetornarOsDadosCorretamente(){

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Veiculo veiculo = veiculo();

        PeriodoData periodoData = new PeriodoData(DateNow.now(), DateNow.now());

        List<Custo> custos = veiculo.getCustos();
        double gastoTotal = custos
                .stream()
                .filter(custo -> custo.getDataPagamento().equals(DateNow.now()))
                .mapToDouble(Custo::getValor)
                .sum();

        List<ReceitaDiaria> receitas = usuario().getReceitasDiarias();
        double ganhoBruto = receitas
                .stream()
                .filter(custo -> custo.getDataReceita().equals(DateNow.now()))
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        double ganhoLiquido = ganhoBruto - gastoTotal;

        Long id = usuarioSecurity.getId();
        String tipo = "dia";
        String dataBase = DateNow.now().toString();

        when(periodoDataHelper.calcularData(tipo, dataBase)).thenReturn(periodoData);
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(veiculoRepository.findByIdVeiculoAndIsAtivo(veiculo.getIdVeiculo(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository
                .findByUsuarioIdUsuarioAndDataReceitaBetween(id, periodoData.dataInicio(), periodoData.dataFim()))
                .thenReturn(receitas);
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(
                veiculo.getIdVeiculo(),
                periodoData.dataInicio(),
                periodoData.dataFim())).thenReturn(custos);

        ResumoFinanceiroPeriodoResponse response = tested.calcularPorPeriodo(tipo, dataBase);

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper).calcularData(tipo, dataBase);
        verify(veiculoRepository).findByIdVeiculoAndIsAtivo(usuarioSecurity.getIdVeiculo(), true);
        verify(receitaDiariaRepository)
                .findByUsuarioIdUsuarioAndDataReceitaBetween(id, periodoData.dataInicio(), periodoData.dataFim());
        verify(custoRepository)
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(
                        veiculo.getIdVeiculo(),
                        periodoData.dataInicio(),
                        periodoData.dataFim());

        assertEquals(ganhoBruto, response.getGanhoBruto());
        assertEquals(ganhoLiquido, response.getLucroLiquido());
        assertEquals(gastoTotal, response.getGastoTotal());
    }

    @Test
    @DisplayName("Não deve retornar os dados se tipo invalido")
    void naoDeveRetornarDadosSeTipoInvalido(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();
        String tipo = "invalido";
        String dataBase = DateNow.now().toString();

        doThrow(ResponseStatusException.class).when(validaTipoPeriodoValidator).porTipo(tipo);

        assertThrows(ResponseStatusException.class, () -> tested.calcularPorPeriodo(tipo, dataBase));

        verify(usuarioAutenticadoService, never()).getUser();
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(veiculoRepository, never()).findByIdVeiculoAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(receitaDiariaRepository, never())
                .findByUsuarioIdUsuarioAndDataReceitaBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository, never())
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }
}
