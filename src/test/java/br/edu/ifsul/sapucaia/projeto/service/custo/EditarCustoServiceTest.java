package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.EditarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoCustoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorPositivoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.*;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.CUSTO;
import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.editarCustoRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.meta;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditarCustoServiceTest {

    @BeforeEach
    void setUp() {
        reset(metaRepository, custoRepository);
    }

    @InjectMocks
    private EditarCustoService tested;

    @Mock
    private ValidaCustoService validaCustoService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ValidaValorPositivoValidator validaValorPositivoValidator;

    @Mock
    private ValidaTipoCustoValidator validaTipoCustoValidator;

    @Mock
    private MetaRepository metaRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Captor
    private ArgumentCaptor<Custo> custoCaptor;

    @Captor
    private ArgumentCaptor<Meta> metaCaptor;

    @Test
    @DisplayName("Deve editar o custo sem metas")
    void deveEditarCustoSemMetas() {

        EditarCustoRequest request = editarCustoRequest();
        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.setValor(request.getValor());

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(usuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());

        Custo response = custoCaptor.getValue();

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com metas")
    void deveEditarCustoComMetas() {

        EditarCustoRequest request = editarCustoRequest();

        if(request.getDataPagamento().getDayOfWeek().equals(MONDAY)){
            request.setDataPagamento(DateNow.now().plusDays(1));
        }

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            if(metasRaiz.get(i).getFormato().equals(DIARIA)
                    && request.getDataPagamento().equals(DateNow.now().plusDays(1))){
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
            else{
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor() + request.getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com metas não diarias")
    void deveEditarCustoComMetasNaoDiarias() {

        EditarCustoRequest request = editarCustoRequest();
        request.setDataPagamento(DateNow.now().with(previousOrSame(MONDAY)));

        if(request.getDataPagamento().equals(DateNow.now())){
            request.setDataPagamento(request.getDataPagamento().plusDays(1));
        }

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            if(metasRaiz.get(i).getFormato().equals(DIARIA)){
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
            else{
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor() + request.getValor();

                if(metasRaiz.get(i).getFormato() == MENSAL && request.getDataPagamento().getMonth() != DateNow.now().getMonth()){
                    valorEsperadoMeta += custo().getValor();
                }

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com metas não semanais")
    void deveEditarCustoComMetasNaoSemanais() {

        EditarCustoRequest request = editarCustoRequest();
        request.setDataPagamento(DateNow.now().minusWeeks(1));

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            if(metasRaiz.get(i).getFormato().equals(DIARIA)){
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
            else if(metasRaiz.get(i).getFormato().equals(SEMANAL)){
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
            else if(metasRaiz.get(i).getFormato().equals(MENSAL)
                    && !(DateNow.now().getMonth().equals(request.getDataPagamento().getMonth())
                    || DateNow.now().getYear() != request.getDataPagamento().getYear())){

                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
            else{
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor() + request.getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com metas não mensais")
    void deveEditarCustoComMetasNaoMensais() {

        EditarCustoRequest request = editarCustoRequest();
        request.setDataPagamento(DateNow.now().minusMonths(1));

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com metas não anual")
    void deveEditarCustoComMetasNaoAnual() {

        EditarCustoRequest request = editarCustoRequest();
        request.setDataPagamento(DateNow.now().minusYears(1));

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com custos não diarios")
    void deveEditarCustoComCustosNaoDiarios() {

        EditarCustoRequest request = editarCustoRequest();

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());

        custo.setDataPagamento(DateNow.now().with(previousOrSame(MONDAY)));

        if(custo.getDataPagamento().equals(DateNow.now())){
            custo.setDataPagamento(custo.getDataPagamento().plusDays(1));
        }

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            if(metasRaiz.get(i).getFormato().equals(DIARIA)){
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
            else{
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor() + request.getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com custos não semanais")
    void deveEditarCustoComCustosNaoSemanais() {

        EditarCustoRequest request = editarCustoRequest();

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());
        custo.setDataPagamento(DateNow.now().minusWeeks(1));

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            if(metasRaiz.get(i).getFormato() == MENSAL
                    && !(!DateNow.now().getMonth().equals(request.getDataPagamento().getMonth())
                    || DateNow.now().getYear() != request.getDataPagamento().getYear())){

                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() - custo().getValor() + request.getValor();

                if(custo.getDataPagamento().minusWeeks(1).getMonth() != request.getDataPagamento().getMonth()){
                    valorEsperadoMeta += custo().getValor();
                }

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
            else{
                double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

                assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
            }
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com custos não mensais")
    void deveEditarCustoComCustosNaoMensais() {

        EditarCustoRequest request = editarCustoRequest();

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());
        custo.setDataPagamento(DateNow.now().minusMonths(1));

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve editar o custo com custos não anual")
    void deveEditarCustoComCustosNaoAnual() {

        EditarCustoRequest request = editarCustoRequest();

        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setUsuario(usuario());
        custo.setDataPagamento(DateNow.now().minusYears(1));

        List<Meta> metas = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

        custo.getVeiculo().getUsuario().setMetas(metas);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(custo.getVeiculo().getUsuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findByIdCustoAndIsAtivo(id, true);
        verify(usuarioAutenticadoService).getUser();
        verify(custoRepository).save(custoCaptor.capture());
        verify(metaRepository, times(3)).save(metaCaptor.capture());

        Custo response = custoCaptor.getValue();

        List<Meta> metasResponse = metaCaptor.getAllValues();

        for(int i = 0; i < metasResponse.size(); i++){

            List<Meta> metasRaiz = List.of(meta(DIARIA, CUSTO), meta(SEMANAL, CUSTO), meta(MENSAL, CUSTO));

            double valorEsperadoMeta = metasRaiz.get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Deve disparar FORBIDDEN se o custo pertencer a outro veículo ou usuário")
    void deveDispararForbiddenQuandoCustoNaoForDoUsuario() {
        EditarCustoRequest request = editarCustoRequest();
        Long id = 1L;

        Custo custo = custo();
        custo.setVeiculo(veiculo());
        custo.getVeiculo().setIdVeiculo(999L);

        UsuarioSecurity usuarioSecurity = new UsuarioSecurity(usuario());

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.of(custo));
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.editar(request));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Você não tem permissão para editar este custo.", exception.getReason());

        verify(custoRepository, never()).save(any(Custo.class));
        verify(metaRepository, never()).save(any(Meta.class));
    }

    @Test
    @DisplayName("Deve disparar NOT_FOUND se o custo não for encontrado")
    void deveDispararNotFoundQuandoCustoNaoExistir() {
        EditarCustoRequest request = editarCustoRequest();
        Long id = request.getIdCusto();

        when(custoRepository.findByIdCustoAndIsAtivo(id, true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.editar(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Custo não encontrado.", exception.getReason());

        verify(usuarioAutenticadoService, never()).getUser();
        verify(custoRepository, never()).save(any(Custo.class));
    }

    @Test
    @DisplayName("Não deve editar o custo se o custo for inválido")
    void naoDeveEditarCustoSeCustoForInvalido() {

        Long idCusto = 1L;
        EditarCustoRequest request = editarCustoRequest();

        doThrow(ResponseStatusException.class).when(validaCustoService).porId(idCusto);

        assertThrows(ResponseStatusException.class, () -> tested.editar(request));

        verify(validaCustoService).porId(idCusto);
        verify(validaValorPositivoValidator, never()).isPositivo(anyDouble());
        verify(validaTipoCustoValidator, never()).tipoValido(anyString());
        verify(custoRepository, never()).findByIdCustoAndIsAtivo(anyLong(), anyBoolean());
        verify(custoRepository, never()).save(any(Custo.class));
    }

    @Test
    @DisplayName("Não deve editar o custo se o valor for inválido")
    void naoDeveEditarCustoSeValorForInvalido() {

        EditarCustoRequest request = editarCustoRequest();
        Long idCusto = request.getIdCusto();

        doThrow(ResponseStatusException.class).when(validaValorPositivoValidator).isPositivo(request.getValor());

        assertThrows(ResponseStatusException.class, () -> tested.editar(request));

        verify(validaCustoService).porId(idCusto);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator, never()).tipoValido(anyString());
        verify(custoRepository, never()).findByIdCustoAndIsAtivo(anyLong(), anyBoolean());
        verify(custoRepository, never()).save(any(Custo.class));
    }

    @Test
    @DisplayName("Não deve editar o custo se o tipo for inválido")
    void naoDeveEditarCustoSeTipoForInvalido() {

        EditarCustoRequest request = editarCustoRequest();
        Long idCusto = request.getIdCusto();

        doThrow(ResponseStatusException.class).when(validaTipoCustoValidator).tipoValido(request.getTipo());

        assertThrows(ResponseStatusException.class, () -> tested.editar(request));

        verify(validaCustoService).porId(idCusto);
        verify(validaValorPositivoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository, never()).findByIdCustoAndIsAtivo(anyLong(), anyBoolean());
        verify(custoRepository, never()).save(any(Custo.class));
    }
}