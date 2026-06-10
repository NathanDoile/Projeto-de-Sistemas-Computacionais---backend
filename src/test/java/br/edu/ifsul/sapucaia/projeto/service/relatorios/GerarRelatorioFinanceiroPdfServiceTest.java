package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.PeriodoRelatorioFinanceiro;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataRelatorioFinanceiroPdfValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.PeriodoRelatorioFinanceiro.MENSAL;
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
    @DisplayName("Deve gerar o relatório corretamente")
    void deveGerarRelatorioCorretamente(){

        Usuario usuario = usuario();
        Veiculo veiculo = veiculo();
        veiculo.setCustos(List.of(custo()));
        veiculo.setUsuario(usuario);

        PeriodoRelatorioFinanceiro periodo = MENSAL;

        LocalDate dataReferencia = LocalDate.now();

        LocalDate dataInicio = dataReferencia.withDayOfMonth(1);
        LocalDate dataFim = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true)).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(List.of(receitaDiaria()));
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(usuario.getIdUsuario(), dataInicio, dataFim)).thenReturn(veiculo.getCustos());

        byte[] response = tested.gerarRelatorioFinanceiro(usuario.getIdUsuario(), dataReferencia, MENSAL);

        verify(validaUsuarioService).porId(usuario.getIdUsuario());
        verify(validaDataRelatorioFinanceiroPdfValidator).naoMaiorQueHoje(dataReferencia);
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getIdUsuario(), true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getIdUsuario(), dataInicio, dataFim);
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(usuario.getIdUsuario(), dataInicio, dataFim);

        assertNotNull(response);
        assertTrue(response.length > 0);
    }
}
