package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
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
import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
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
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaTipoPeriodoValidator validaTipoPeriodoValidator;

    @Mock
    private PeriodoDataHelper periodoDataHelper;

    @Test
    @DisplayName("Deve retornar os dados corretamente")
    void deveRetornarOsDadosCorretamente(){

        Usuario usuario = usuario();

        PeriodoData periodoData = new PeriodoData(DateNow.now(), DateNow.now());

        List<Custo> custos = usuario.getVeiculo().getCustos();
        double gastoTotal = custos
                .stream()
                .filter(custo -> custo.getDataPagamento().equals(DateNow.now()))
                .mapToDouble(Custo::getValor)
                .sum();

        List<ReceitaDiaria> receitas = usuario.getReceitasDiarias();
        double ganhoBruto = receitas
                .stream()
                .filter(custo -> custo.getDataReceita().equals(DateNow.now()))
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        double ganhoLiquido = ganhoBruto - gastoTotal;

        Long id = usuario.getIdUsuario();
        String tipo = "dia";
        String dataBase = DateNow.now().toString();

        when(periodoDataHelper.calcularData(tipo, dataBase)).thenReturn(periodoData);
        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(receitaDiariaRepository
                .findByUsuarioIdUsuarioAndDataReceitaBetween(id, periodoData.dataInicio(), periodoData.dataFim()))
                .thenReturn(receitas);
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(
                usuario.getVeiculo().getIdVeiculo(),
                periodoData.dataInicio(),
                periodoData.dataFim())).thenReturn(custos);

        ResumoFinanceiroPeriodoResponse response = tested.calcularPorPeriodo(id, tipo, dataBase);

        verify(validaUsuarioService).porId(id);
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper).calcularData(tipo, dataBase);
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(receitaDiariaRepository)
                .findByUsuarioIdUsuarioAndDataReceitaBetween(id, periodoData.dataInicio(), periodoData.dataFim());
        verify(custoRepository)
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(
                        usuario.getVeiculo().getIdVeiculo(),
                        periodoData.dataInicio(),
                        periodoData.dataFim());

        assertEquals(ganhoBruto, response.getGanhoBruto());
        assertEquals(ganhoLiquido, response.getLucroLiquido());
        assertEquals(gastoTotal, response.getGastoTotal());
    }

    @Test
    @DisplayName("Não deve retornar os dados se id do usuario invalido")
    void naoDeveRetornarDadosSeIdUsuarioInvalido(){

        Long id = 5L;
        String tipo = "dia";
        String dataBase = DateNow.now().toString();

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.calcularPorPeriodo(id, tipo, dataBase));

        verify(validaUsuarioService).porId(id);
        verify(validaTipoPeriodoValidator, never()).porTipo(any(String.class));
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(receitaDiariaRepository, never())
                .findByUsuarioIdUsuarioAndDataReceitaBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository, never())
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Não deve retornar os dados se tipo invalido")
    void naoDeveRetornarDadosSeTipoInvalido(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();
        String tipo = "invalido";
        String dataBase = DateNow.now().toString();

        doThrow(ResponseStatusException.class).when(validaTipoPeriodoValidator).porTipo(tipo);

        assertThrows(ResponseStatusException.class, () -> tested.calcularPorPeriodo(id, tipo, dataBase));

        verify(validaUsuarioService).porId(id);
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(receitaDiariaRepository, never())
                .findByUsuarioIdUsuarioAndDataReceitaBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository, never())
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }
}
