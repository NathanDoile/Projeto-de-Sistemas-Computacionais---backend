package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.receitaDiaria;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InformacoesDaSemanaServiceTest {

    @InjectMocks
    private InformacoesDaSemanaService tested;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Deve retornar as informações da semana corretamente")
    void deveRetornarInformacoesSemanaCorretamente(){

        UsuarioSecurity usuario = usuarioSecurity();

        Long id = usuario.getId();

        Veiculo veiculo = veiculo();

        List<ReceitaDiaria> receitas = of(receitaDiaria());

        List<Custo> custos = of(custo());

        when(usuarioAutenticadoService.getUser()).thenReturn(usuario);
        when(veiculoRepository.findByIdVeiculo(usuario.getIdVeiculo())).thenReturn(veiculo);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(eq(id), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(receitas);
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(eq(usuario.getIdVeiculo()),
                any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(custos);

        InformacoesDaSemanaResponse response = tested.buscarInformacoesDaSemana();

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByIdVeiculo(veiculo.getIdVeiculo());
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(eq(id), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(eq(usuario.getIdVeiculo()), any(LocalDate.class), any(LocalDate.class));

        double ganhoBrutoEsperado = receitas
                .stream()
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        double despesaTotalEsperado = custos
                .stream()
                .mapToDouble(Custo::getValor)
                .sum();

        double lucroLiquidoEsperado = ganhoBrutoEsperado - despesaTotalEsperado;

        assertEquals(ganhoBrutoEsperado, response.getGanhoBruto());
        assertEquals(despesaTotalEsperado, response.getDespesaTotal());
        assertEquals(lucroLiquidoEsperado, response.getLucroLiquido());
        assertEquals(veiculo.getKmAtual(), response.getKmAtual());
    }
}
