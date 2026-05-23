package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.factory.CustoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarCustosEmAbertoServiceTest {

    @InjectMocks
    private BuscarCustosEmAbertoService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve buscar os custos em aberto corretamente")
    void deveBuscarCustosEmAbertoCorretamente() {

        Long id = 1L;

        Usuario usuario = usuario();

        Custo custo = CustoFactory.custo();
        custo.setDataPagamento(null);
        custo.setVeiculo(usuario.getVeiculo());

        Veiculo veiculo = custo.getVeiculo();
        List<Custo> custos = List.of(custo);

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(custoRepository.findByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true))
                .thenReturn(custos);

        List<BuscarCustosEmAbertoResponse> response = tested.buscar(id);

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(custoRepository).findByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true);

        assertEquals(custos.size(), response.size());
        for (int i = 0; i < response.size(); i++) {
            assertEquals(custos.get(i).getIdCusto(), response.get(i).getIdCusto());
            assertEquals(custos.get(i).getDescricao(), response.get(i).getDescricao());
            assertEquals(custos.get(i).getValor(), response.get(i).getValor());
        }
    }

    @Test
    @DisplayName("Não deve buscar os custos em aberto se o veículo for inválido")
    void naoDeveBuscarCustosSeVeiculoForInvalido() {

        Long id = 1L;

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.buscar(id));

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(anyLong(), anyBoolean());
        verify(custoRepository, never()).findByVeiculoAndDataPagamentoIsNullAndIsAtivo(any(Veiculo.class), anyBoolean());
    }
}
