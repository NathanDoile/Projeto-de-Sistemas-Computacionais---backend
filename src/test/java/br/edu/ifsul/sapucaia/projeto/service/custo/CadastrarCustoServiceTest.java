package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoCustoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.cadastrarCustoRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarCustoServiceTest {

    @InjectMocks
    private CadastrarCustoService tested;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ValidaValorCustoValidator validaValorCustoValidator;

    @Mock
    private ValidaTipoCustoValidator validaTipoCustoValidator;

    @Captor
    private ArgumentCaptor<Custo> custoCaptor;

    @Test
    @DisplayName("Deve cadastrar custo")
    void cadastraCustoComValoresCorretos(){

        CadastrarCustoRequest request = cadastrarCustoRequest();

        Veiculo veiculoMock = veiculo();

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true))
                .thenReturn(veiculoMock);

        tested.cadastrar(request);

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository)
                .findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);
        verify(custoRepository).save(custoCaptor.capture());

        Custo custoResponse = custoCaptor.getValue();

        assertEquals(TipoCusto.deTexto(request.getTipo()), custoResponse.getTipo());
        assertEquals(request.getValor(), custoResponse.getValor());
        assertEquals(request.getDescricao(), custoResponse.getDescricao());
        assertEquals(request.getDataVencimento(), custoResponse.getDataVencimento());
        assertEquals(request.getDataPagamento(), custoResponse.getDataPagamento());
        assertEquals(veiculoMock, custoResponse.getVeiculo());
        assertTrue(custoResponse.isAtivo());
    }

    @Test
    @DisplayName("Nao deve cadastrar custo com id veiculo errado")
    void naoCadastraCustoComIdVeiculoErrado(){

        CadastrarCustoRequest request = cadastrarCustoRequest();

        doThrow(ResponseStatusException.class)
                .when(validaVeiculoService)
                .porId(request.getIdVeiculo());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator, never()).isPositivo(any(Double.class));
        verify(validaTipoCustoValidator, never()).tipoValido(any(String.class));
        verify(veiculoRepository, never())
                .findByIdVeiculoAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(custoRepository, never()).save(any(Custo.class));
    }

    @Test
    @DisplayName("Nao deve cadastrar custo com valor menor ou igual a zero")
    void naoCadastraCustoComValorMenorIgualZero(){

        CadastrarCustoRequest request = CadastrarCustoRequest
                .builder()
                .idVeiculo(1L)
                .tipo("COMBUSTIVEL")
                .valor(0.00)
                .descricao("Custo de teste")
                .dataVencimento(now().plusDays(5))
                .dataPagamento(now())
                .build();

        doThrow(ResponseStatusException.class)
                .when(validaValorCustoValidator)
                .isPositivo(request.getValor());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator, never()).tipoValido(any(String.class));
        verify(veiculoRepository, never())
                .findByIdVeiculoAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(custoRepository, never()).save(any(Custo.class));
    }

    @Test
    @DisplayName("Nao deve cadastrar custo com tipo invalido")
    void naoCadastraCustoComTipoInvalido(){

        CadastrarCustoRequest request = CadastrarCustoRequest
                .builder()
                .idVeiculo(1L)
                .tipo("TIPO_INVALIDO")
                .valor(100.00)
                .descricao("Custo de teste")
                .dataVencimento(now().plusDays(5))
                .dataPagamento(now())
                .build();

        doThrow(ResponseStatusException.class)
                .when(validaTipoCustoValidator)
                .tipoValido(request.getTipo());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository, never())
                .findByIdVeiculoAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(custoRepository, never()).save(any(Custo.class));
    }
}