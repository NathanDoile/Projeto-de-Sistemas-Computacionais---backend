package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.factory.CustoFactory;
import br.edu.ifsul.sapucaia.projeto.factory.ManutencaoFactory;
import br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory;
import br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.custo.CadastrarCustoService;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private VeiculoRepository veiculoRepository;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ManutencaoRepository manutencaoRepository;

    @Mock
    private CadastrarCustoService cadastrarCustoService;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Captor
    private ArgumentCaptor<Manutencao> manutencaoCaptor;

    @Captor
    private ArgumentCaptor<Veiculo> veiculoCaptor;

    @Captor
    private ArgumentCaptor<Custo> custoCaptor;

    @Test
    @DisplayName("Deve cadastrar a manutenção corretamente")
    void deveCadastrarManutencaoCorretamente() {

        UsuarioSecurity usuarioSecurity = UsuarioFactory.usuarioSecurity();

        when(usuarioAutenticadoService.getUser())
                .thenReturn(usuarioSecurity);

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

        when(veiculoRepository.findByUsuarioIdUsuario(anyLong()))
                .thenReturn(veiculo);

        when(custoRepository.findByIdCustoAndIsAtivo(anyLong(), eq(true)))
                .thenReturn(Optional.of(custo));

        tested.cadastrar(request);

        verify(manutencaoRepository).save(manutencaoCaptor.capture());
    }

    @Test
    @DisplayName("Não deve cadastrar a manutenção se o tipo for inválido")
    void naoDeveCadastrarManutencaoSeTipoForInvalido() {

        CadastrarManutencaoRequest request = ManutencaoFactory.cadastrarManutencaoRequest();

        doThrow(ResponseStatusException.class)
                .when(validaTipoManutencaoValidator)
                .tipoValido(request.getTipo());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(usuarioAutenticadoService, never()).getUser();
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

        verify(usuarioAutenticadoService, never()).getUser();
        verify(manutencaoRepository, never()).save(any());
    }
}