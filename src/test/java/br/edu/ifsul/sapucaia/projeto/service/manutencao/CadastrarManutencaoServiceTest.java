package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.factory.CustoFactory;
import br.edu.ifsul.sapucaia.projeto.factory.ManutencaoFactory;
import br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.custo.CadastrarCustoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataManutencaoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoManutencaoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarManutencaoServiceTest {

    @InjectMocks
    private CadastrarManutencaoService tested;

    @Mock
    private ValidaTipoManutencaoValidator validaTipoManutencaoValidator;

    @Mock
    private ValidaDataManutencaoValidator validadataManutencaoValidator;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ManutencaoRepository manutencaoRepository;

    @Mock
    private CadastrarCustoService cadastrarCustoService;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private UsuarioSecurity usuarioSecurity;

    @Captor
    private ArgumentCaptor<Manutencao> manutencaoCaptor;

    @Captor
    private ArgumentCaptor<Veiculo> veiculoCaptor;

    @Captor
    private ArgumentCaptor<Custo> custoCaptor;

    private void mockUsuario() {
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(usuarioSecurity.getId()).thenReturn(1L);
    }

    @Test
    @DisplayName("Deve cadastrar a manutenção corretamente")
    void deveCadastrarManutencaoCorretamente() {

        mockUsuario();

        CadastrarManutencaoRequest request = ManutencaoFactory.cadastrarManutencaoRequest();

        Veiculo veiculo = VeiculoFactory.veiculo();
        veiculo.setManutencoes(new ArrayList<>());

        Custo custo = CustoFactory.custo();
        custo.setManutencao(null);

        BuscarCustosEmAbertoResponse responseCusto =
                BuscarCustosEmAbertoResponse.builder()
                        .idCusto(1L)
                        .descricao(request.getDescricao())
                        .dataVencimento(request.getDataManutencao())
                        .valor(request.getValor())
                        .tipo(request.getTipo())
                        .build();

        when(cadastrarCustoService.cadastrar(any())).thenReturn(responseCusto);

        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(anyLong(), eq(true)))
                .thenReturn(veiculo);

        when(custoRepository.findByIdCustoAndIsAtivo(anyLong(), eq(true)))
                .thenReturn(Optional.of(custo));

        tested.cadastrar(request);

        verify(manutencaoRepository).save(manutencaoCaptor.capture());
    }

    @Test
    @DisplayName("Não deve cadastrar a manutenção se o veículo for inválido")
    void naoDeveCadastrarManutencaoSeVeiculoForInvalido() {

        mockUsuario();

        CadastrarManutencaoRequest request = ManutencaoFactory.cadastrarManutencaoRequest();

        doThrow(ResponseStatusException.class)
                .when(validaVeiculoService)
                .porIdUsuario(1L);

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(manutencaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve cadastrar a manutenção se o tipo for inválido")
    void naoDeveCadastrarManutencaoSeTipoForInvalido() {

        CadastrarManutencaoRequest request = ManutencaoFactory.cadastrarManutencaoRequest();

        doThrow(ResponseStatusException.class)
                .when(validaTipoManutencaoValidator)
                .tipoValido(request.getTipo());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(manutencaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve cadastrar a manutenção se a data for inválida")
    void naoDeveCadastrarManutencaoSeDataForInvalida() {

        CadastrarManutencaoRequest request = ManutencaoFactory.cadastrarManutencaoRequest();

        doThrow(ResponseStatusException.class)
                .when(validadataManutencaoValidator)
                .dataMenorQueHoje(request.getDataManutencao());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(manutencaoRepository, never()).save(any());
    }
}