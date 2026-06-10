package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaDoMesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
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

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto.*;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static java.time.LocalDate.of;
import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GastosPorCategoriaDoPeriodoServiceTest {

    @InjectMocks
    private GastosPorCategoriaDoPeriodoService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaTipoPeriodoValidator validaTipoPeriodoValidator;

    @Mock
    private PeriodoDataHelper periodoDataHelper;

    @Test
    @DisplayName("Deve retornar os gastos por categoria corretamente")
    void deveRetornarGastosCategoriaCorretamente(){

        Usuario usuario = usuario();

        PeriodoData periodoData = new PeriodoData(of(2026, MAY, 20), of(20, MAY, 20));

        List<Custo> custos = usuario.getVeiculo().getCustos();

        Long id = usuario.getIdUsuario();
        String tipo = "dia";
        String dataBase = "2026-05-20";

        when(periodoDataHelper.calcularData(tipo, dataBase)).thenReturn(periodoData);
        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(custoRepository
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(
                        usuario.getVeiculo().getIdVeiculo(), periodoData.dataInicio(), periodoData.dataFim()))
                .thenReturn(custos);

        GastosPorCategoriaDoMesResponse response = tested.calcularPorPeriodo(id, tipo, dataBase);

        verify(validaUsuarioService).porId(id);
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper).calcularData(tipo, dataBase);
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(
                usuario.getVeiculo().getIdVeiculo(), periodoData.dataInicio(), periodoData.dataFim());

        assertEquals(custos
                .stream()
                .filter(custo -> custo.getTipo().equals(COMBUSTIVEL))
                .mapToDouble(Custo::getValor)
                .sum(), response.getCombustivel());

        assertEquals(custos
                .stream()
                .filter(custo -> custo.getTipo().equals(MANUTENCAO))
                .mapToDouble(Custo::getValor)
                .sum(), response.getManutencao());

        assertEquals(custos
                .stream()
                .filter(custo -> custo.getTipo().equals(SEGURO))
                .mapToDouble(Custo::getValor)
                .sum(), response.getSeguro());

        assertEquals(custos
                .stream()
                .filter(custo -> custo.getTipo().equals(IMPOSTOS))
                .mapToDouble(Custo::getValor)
                .sum(), response.getImpostos());

        assertEquals(custos
                .stream()
                .filter(custo -> custo.getTipo().equals(MULTAS))
                .mapToDouble(Custo::getValor)
                .sum(), response.getMultas());

        assertEquals(custos
                .stream()
                .filter(custo -> custo.getTipo().equals(OUTROS))
                .mapToDouble(Custo::getValor)
                .sum(), response.getOutros());
    }

    @Test
    @DisplayName("Não deve retornar os gastos por categoria se id do usuario incorreto")
    void naoDeveRetornarGastosCategoriaSeIdUsuarioIncorreto(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();
        String tipo = "dia";
        String dataBase = "2026-05-20";

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.calcularPorPeriodo(id, tipo, dataBase));

        verify(validaUsuarioService).porId(id);
        verify(validaTipoPeriodoValidator, never()).porTipo(any(String.class));
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(custoRepository, never()).findByVeiculoIdVeiculoAndDataPagamentoBetween(
                any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Não deve retornar os gastos por categoria se tipo inválido")
    void naoDeveRetornarGastosCategoriaSeTipoInvalido(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();
        String tipo = "dia";
        String dataBase = "2026-05-20";

        doThrow(ResponseStatusException.class).when(validaTipoPeriodoValidator).porTipo(tipo);

        assertThrows(ResponseStatusException.class, () -> tested.calcularPorPeriodo(id, tipo, dataBase));

        verify(validaUsuarioService).porId(id);
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(custoRepository, never()).findByVeiculoIdVeiculoAndDataPagamentoBetween(
                any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }
}
