package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
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
    private UsuarioAutenticadoService usuarioAutenticadoService;

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

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        PeriodoData periodoData = new PeriodoData(of(2026, MAY, 20), of(20, MAY, 20));

        Usuario usuario = usuario();
        usuario.setIdUsuario(usuarioSecurity.getId());

        List<Custo> custos = usuario.getVeiculo().getCustos();

        String tipo = "dia";
        String dataBase = "2026-05-20";

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(periodoDataHelper.calcularData(tipo, dataBase)).thenReturn(periodoData);
        when(custoRepository
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(
                        usuarioSecurity.getIdVeiculo(), periodoData.dataInicio(), periodoData.dataFim()))
                .thenReturn(custos);

        GastosPorCategoriaResponse response = tested.calcularPorPeriodo(tipo, dataBase);


        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper).calcularData(tipo, dataBase);
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
    @DisplayName("Não deve retornar os gastos por categoria se tipo inválido")
    void naoDeveRetornarGastosCategoriaSeTipoInvalido(){

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        String tipo = "dia";
        String dataBase = "2026-05-20";

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        doThrow(ResponseStatusException.class).when(validaTipoPeriodoValidator).porTipo(tipo);

        assertThrows(ResponseStatusException.class, () -> tested.calcularPorPeriodo(tipo, dataBase));

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoPeriodoValidator).porTipo(tipo);
        verify(periodoDataHelper, never()).calcularData(any(String.class), any(String.class));
        verify(custoRepository, never()).findByVeiculoIdVeiculoAndDataPagamentoBetween(
                any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }
}
