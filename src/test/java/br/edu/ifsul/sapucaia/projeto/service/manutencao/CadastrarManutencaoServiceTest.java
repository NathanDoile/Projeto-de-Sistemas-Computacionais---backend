package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;
import br.edu.ifsul.sapucaia.projeto.factory.CustoFactory;
import br.edu.ifsul.sapucaia.projeto.factory.ManutencaoFactory;
import br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoPossuiManutencaoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoManutencaoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidadataManutencaoValidator;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarManutencaoServiceTest {

    @InjectMocks
    private CadastrarManutencaoService tested;

    @Mock
    private ValidaTipoManutencaoValidator validaTipoManutencaoValidator;

    @Mock
    private ValidadataManutencaoValidator validadataManutencaoValidator;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private ValidaCustoService validaCustoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ManutencaoRepository manutencaoRepository;

    @Mock
    private ValidaCustoPossuiManutencaoService validaCustoPossuiManutencaoService;

    @Captor
    private ArgumentCaptor<Manutencao> manutencaoCaptor;

    @Captor
    private ArgumentCaptor<Veiculo> veiculoCaptor;

    @Captor
    private ArgumentCaptor<Custo> custoCaptor;

    @Test
    @DisplayName("Deve cadastrar manutenção corretamente")
    void deveCadastrarManutencaoCorretamente() {

        CadastrarManutencaoRequest request = ManutencaoFactory.cadastrarManutencaoRequest();
        Long idVeiculo = request.getIdVeiculo();
        Long idCusto = request.getIdCusto();

        Veiculo veiculo = VeiculoFactory.veiculo();
        veiculo.setManutencoes(new ArrayList<>());
        Custo custo = CustoFactory.custo();
        custo.setManutencao(null);

        when(veiculoRepository.findById(idVeiculo)).thenReturn(Optional.of(veiculo));
        when(custoRepository.findById(idCusto)).thenReturn(Optional.of(custo));

        tested.cadastrar(request);

        verify(validaTipoManutencaoValidator).tipoValido(request.getTipo());
        verify(validadataManutencaoValidator).dataMenorQueHoje(request.getDataManutencao());
        verify(validaVeiculoService).porId(idVeiculo);
        verify(validaCustoService).porId(idCusto);
        verify(validaCustoPossuiManutencaoService).porId(idCusto);
        verify(manutencaoRepository).save(manutencaoCaptor.capture());
        verify(veiculoRepository).save(veiculoCaptor.capture());
        verify(custoRepository).save(custoCaptor.capture());

        Manutencao savedManutencao = manutencaoCaptor.getValue();
        Veiculo savedVeiculo = veiculoCaptor.getValue();
        Custo savedCusto = custoCaptor.getValue();

        assertEquals(TipoManutencao.PREVENTIVA, savedManutencao.getTipo());
        assertEquals(request.getDescricao(), savedManutencao.getDescricao());
        assertEquals(idVeiculo, savedVeiculo.getIdVeiculo());
        assertTrue(savedVeiculo.getManutencoes().contains(savedManutencao));
        assertEquals(savedManutencao, savedCusto.getManutencao());
    }

    @Test
    @DisplayName("Não deve cadastrar manutenção se veículo for inválido")
    void naoDeveCadastrarManutencaoSeVeiculoForInvalido() {

        CadastrarManutencaoRequest request = ManutencaoFactory.cadastrarManutencaoRequest();
        Long idVeiculo = request.getIdVeiculo();

        doThrow(ResponseStatusException.class).when(validaVeiculoService).porId(idVeiculo);

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(validaTipoManutencaoValidator).tipoValido(request.getTipo());
        verify(validadataManutencaoValidator).dataMenorQueHoje(request.getDataManutencao());
        verify(validaVeiculoService).porId(idVeiculo);
        verify(validaCustoService, never()).porId(anyLong());
        verify(veiculoRepository, never()).findById(anyLong());
        verify(custoRepository, never()).findById(anyLong());
        verify(manutencaoRepository, never()).save(any(Manutencao.class));
    }
}
