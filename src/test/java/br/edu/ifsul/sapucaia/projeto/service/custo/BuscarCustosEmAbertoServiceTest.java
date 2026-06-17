package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.factory.CustoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarCustosEmAbertoServiceTest {

    @InjectMocks
    private BuscarCustosEmAbertoService tested;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Test
    @DisplayName("Deve buscar os custos em aberto corretamente")
    void deveBuscarCustosEmAbertoCorretamente() {

        Pageable pageable = PageRequest.of(0, 10);

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Veiculo veiculo = veiculo();

        Custo custo = CustoFactory.custo();
        custo.setDataPagamento(null);
        custo.setVeiculo(veiculo);

        Page<Custo> custos = new PageImpl<>(List.of(custo), pageable, 1);

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(veiculoRepository.findByIdVeiculoAndIsAtivo(usuarioSecurity.getIdVeiculo(), true)).thenReturn(veiculo);
        when(custoRepository.findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true, pageable))
                .thenReturn(custos);

        Page<BuscarCustosEmAbertoResponse> response = tested.buscar(pageable);

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByIdVeiculoAndIsAtivo(veiculo.getIdVeiculo(), true);
        verify(custoRepository).findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true, pageable);

        assertEquals(custos.getTotalElements(), response.getTotalElements());
        for (int i = 0; i < response.getContent().size(); i++) {
            assertEquals(custos.getContent().get(i).getIdCusto(), response.getContent().get(i).getIdCusto());
            assertEquals(custos.getContent().get(i).getDescricao(), response.getContent().get(i).getDescricao());
            assertEquals(custos.getContent().get(i).getValor(), response.getContent().get(i).getValor());
        }
    }
}
