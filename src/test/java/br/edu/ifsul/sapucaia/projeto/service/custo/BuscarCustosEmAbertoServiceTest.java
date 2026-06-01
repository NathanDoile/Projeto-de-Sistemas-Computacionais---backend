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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Pageable pageable = PageRequest.of(0, 10);

        Usuario usuario = usuario();

        Custo custo = CustoFactory.custo();
        custo.setDataPagamento(null);
        custo.setVeiculo(usuario.getVeiculo());

        Veiculo veiculo = custo.getVeiculo();
        Page<Custo> custos = new PageImpl<>(List.of(custo), pageable, 1);

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));
        when(custoRepository.findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true, pageable))
                .thenReturn(custos);

        Page<BuscarCustosEmAbertoResponse> response = tested.buscar(id, pageable);

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(custoRepository).findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true, pageable);

        assertEquals(custos.getTotalElements(), response.getTotalElements());
        for (int i = 0; i < response.getContent().size(); i++) {
            assertEquals(custos.getContent().get(i).getIdCusto(), response.getContent().get(i).getIdCusto());
            assertEquals(custos.getContent().get(i).getDescricao(), response.getContent().get(i).getDescricao());
            assertEquals(custos.getContent().get(i).getValor(), response.getContent().get(i).getValor());
        }
    }

    @Test
    @DisplayName("Não deve buscar os custos em aberto se o veículo for inválido")
    void naoDeveBuscarCustosSeVeiculoForInvalido() {

        Long id = 1L;

        Pageable pageable = PageRequest.of(0, 10);

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.buscar(id, pageable));

        verify(validaUsuarioService).porId(id);
        verify(usuarioRepository, never()).findByIdUsuarioAndIsAtivo(anyLong(), anyBoolean());
        verify(custoRepository, never()).findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(any(Veiculo.class), anyBoolean(), any(Pageable.class));
    }
}
