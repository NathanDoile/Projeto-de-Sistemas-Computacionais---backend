package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
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

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.receitaDiaria;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InformacoesDaSemanaServiceTest {

    @InjectMocks
    private InformacoesDaSemanaService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar as informações da semana corretamente")
    void deveRetornarInformacoesSemanaCorretamente(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        List<ReceitaDiaria> receitas = of(receitaDiaria());

        List<Custo> custos = of(custo());

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(eq(id), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(receitas);
        when(custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(eq(usuario.getVeiculo().getIdVeiculo()),
                any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(custos);

        InformacoesDaSemanaResponse response = tested.buscarInformacoesDaSemana(id);

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(eq(id), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository).findByVeiculoIdVeiculoAndDataPagamentoBetween(eq(usuario.getVeiculo().getIdVeiculo()), any(LocalDate.class), any(LocalDate.class));

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
        assertEquals(usuario.getVeiculo().getKmAtual(), response.getKmAtual());
    }

    @Test
    @DisplayName("Não deve retornar as informações da semana se id do usuario incorreto")
    void naoDeveRetornarInformacoesSemanaSeIdUsuarioIncorreto(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.buscarInformacoesDaSemana(id));

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(receitaDiariaRepository, never()).findByUsuarioIdUsuarioAndDataReceitaBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
        verify(custoRepository, never()).findByVeiculoIdVeiculoAndDataPagamentoBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }
}
