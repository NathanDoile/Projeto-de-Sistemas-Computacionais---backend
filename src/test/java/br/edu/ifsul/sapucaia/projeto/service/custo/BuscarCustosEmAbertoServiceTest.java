package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.factory.CustoFactory;
import br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarCustosEmAbertoServiceTest {

    @InjectMocks
    private BuscarCustosEmAbertoService tested;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Deve buscar custos em aberto corretamente")
    void deveBuscarCustosEmAbertoCorretamente() {

        Long id = 1L;
        Veiculo veiculo = VeiculoFactory.veiculo();
        Custo custo = CustoFactory.custo();
        custo.setDataPagamento(null);
        custo.setVeiculo(veiculo);

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(id, true)).thenReturn(veiculo);
        when(custoRepository.findByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true))
                .thenReturn(List.of(custo));

        List<BuscarCustosEmAbertoResponse> response = tested.buscar(id);

        verify(validaVeiculoService).porId(id);
        verify(veiculoRepository).findByIdVeiculoAndIsAtivo(id, true);
        verify(custoRepository).findByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true);

        assertEquals(1, response.size());
        assertEquals(custo.getIdCusto(), response.get(0).getIdCusto());
        assertEquals(custo.getDescricao(), response.get(0).getDescricao());
        assertEquals(custo.getValor(), response.get(0).getValor());
    }

    @Test
    @DisplayName("Não deve buscar custos em aberto se veículo for inválido")
    void naoDeveBuscarCustosSeVeiculoForInvalido() {

        Long id = 1L;

        doThrow(ResponseStatusException.class).when(validaVeiculoService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.buscar(id));

        verify(validaVeiculoService).porId(id);
        verify(veiculoRepository, never()).findByIdVeiculoAndIsAtivo(anyLong(), anyBoolean());
        verify(custoRepository, never()).findByVeiculoAndDataPagamentoIsNullAndIsAtivo(any(Veiculo.class), anyBoolean());
    }
}
