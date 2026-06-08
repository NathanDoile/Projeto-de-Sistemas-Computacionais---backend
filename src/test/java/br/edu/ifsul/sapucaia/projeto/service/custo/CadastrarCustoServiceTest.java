package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoCustoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.*;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.CUSTO;
import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.cadastrarCustoRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.meta;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static java.time.DayOfWeek.MONDAY;
import static java.time.LocalDate.now;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarCustoServiceTest {

    @BeforeEach
    void setUp() {
        reset(metaRepository, custoRepository);
    }

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

    @Mock
    private MetaRepository metaRepository;

    @Captor
    private ArgumentCaptor<Custo> custoCaptor;

    @Captor
    private ArgumentCaptor<Meta> metaCaptor;

    @Test
    @DisplayName("Deve cadastrar custo sem metas")
    void deveCadastraCustoSemMetas(){

        CadastrarCustoRequest request = cadastrarCustoRequest();
        request.setDataPagamento(null);

        Veiculo veiculoMock = veiculo();

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true))
                .thenReturn(veiculoMock);

        BuscarCustosEmAbertoResponse response = tested.cadastrar(request);

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository)
                .findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, never()).save(any(Meta.class));

        assertEquals(TipoCusto.deTexto(request.getTipo()), TipoCusto.deTexto(response.getTipo()));
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDescricao(), response.getDescricao());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
    }

    @Test
    @DisplayName("Deve cadastrar custo com metas")
    void deveCadastraCustoComMetas(){

        CadastrarCustoRequest request = cadastrarCustoRequest();

        if(request.getDataPagamento().getDayOfWeek().equals(MONDAY)){
            request.setDataPagamento(now().plusDays(1));
        }

        Veiculo veiculoMock = veiculo();
        veiculoMock.setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        veiculoMock.getUsuario().setMetas(metas);

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true))
                .thenReturn(veiculoMock);

        BuscarCustosEmAbertoResponse response = tested.cadastrar(request);

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository)
                .findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);
        verify(metaRepository, times(3)).save(metaCaptor.capture());
        verify(custoRepository).save(custoCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.deTexto(request.getTipo()), TipoCusto.deTexto(response.getTipo()));
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDescricao(), response.getDescricao());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
    }

    @Test
    @DisplayName("Deve cadastrar custo com metas não diarias")
    void deveCadastraCustoComMetasNaoDiarias(){

        CadastrarCustoRequest request = cadastrarCustoRequest();
        request.setDataPagamento(now().with(previousOrSame(MONDAY)));

        if(request.getDataPagamento().equals(now())){
            request.getDataPagamento().plusDays(1);
        }

        Veiculo veiculoMock = veiculo();
        veiculoMock.setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        veiculoMock.getUsuario().setMetas(metas);

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true))
                .thenReturn(veiculoMock);

        BuscarCustosEmAbertoResponse response = tested.cadastrar(request);

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository)
                .findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);
        verify(metaRepository, times(2)).save(metaCaptor.capture());
        verify(custoRepository).save(custoCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.deTexto(request.getTipo()), TipoCusto.deTexto(response.getTipo()));
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDescricao(), response.getDescricao());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
    }

    @Test
    @DisplayName("Deve cadastrar custo com metas não semanais")
    void deveCadastraCustoComMetasNaoSemanais(){

        CadastrarCustoRequest request = cadastrarCustoRequest();
        request.setDataPagamento(now().minusWeeks(1));

        Veiculo veiculoMock = veiculo();
        veiculoMock.setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        veiculoMock.getUsuario().setMetas(metas);

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true))
                .thenReturn(veiculoMock);

        BuscarCustosEmAbertoResponse response = tested.cadastrar(request);

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository)
                .findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);
        if(now().getMonth() == request.getDataPagamento().getMonth() && now().getYear() == request.getDataPagamento().getYear()){
            verify(metaRepository, times(1)).save(metaCaptor.capture());
        }
        else{
            verify(metaRepository, never()).save(metaCaptor.capture());
        }
        verify(custoRepository).save(custoCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.deTexto(request.getTipo()), TipoCusto.deTexto(response.getTipo()));
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDescricao(), response.getDescricao());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
    }

    @Test
    @DisplayName("Deve cadastrar custo com metas não mensais")
    void deveCadastraCustoComMetasNaoMensais(){

        CadastrarCustoRequest request = cadastrarCustoRequest();
        request.setDataPagamento(now().minusMonths(1));

        Veiculo veiculoMock = veiculo();
        veiculoMock.setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        veiculoMock.getUsuario().setMetas(metas);

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true))
                .thenReturn(veiculoMock);

        BuscarCustosEmAbertoResponse response = tested.cadastrar(request);

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository)
                .findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);
        verify(metaRepository, never()).save(metaCaptor.capture());
        verify(custoRepository).save(custoCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.deTexto(request.getTipo()), TipoCusto.deTexto(response.getTipo()));
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDescricao(), response.getDescricao());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
    }

    @Test
    @DisplayName("Deve cadastrar custo com metas não anual")
    void deveCadastraCustoComMetasNaoAnual(){

        CadastrarCustoRequest request = cadastrarCustoRequest();
        request.setDataPagamento(now().minusYears(1));

        Veiculo veiculoMock = veiculo();
        veiculoMock.setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        veiculoMock.getUsuario().setMetas(metas);

        when(veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true))
                .thenReturn(veiculoMock);

        BuscarCustosEmAbertoResponse response = tested.cadastrar(request);

        verify(validaVeiculoService).porId(request.getIdVeiculo());
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(veiculoRepository)
                .findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);
        verify(metaRepository, never()).save(metaCaptor.capture());
        verify(custoRepository).save(custoCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.deTexto(request.getTipo()), TipoCusto.deTexto(response.getTipo()));
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDescricao(), response.getDescricao());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
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