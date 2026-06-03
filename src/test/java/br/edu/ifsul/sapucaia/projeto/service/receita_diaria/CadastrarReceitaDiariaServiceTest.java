package br.edu.ifsul.sapucaia.projeto.service.receita_diaria;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataReceitaDiariaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorReceitaDiariaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.cadastrarReceitaDiariaRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static java.time.DayOfWeek.MONDAY;
import static java.time.LocalDate.now;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarReceitaDiariaServiceTest {

    @InjectMocks
    private CadastrarReceitaDiariaService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private ValidaValorReceitaDiariaValidator validaValorReceitaDiariaValidator;

    @Mock
    private ValidaDataReceitaDiariaValidator validaDataReceitaDiariaValidator;

    @Mock
    private MetaRepository metaRepository;

    @Captor
    private ArgumentCaptor<ReceitaDiaria> receitaDiariaCaptor;

    @Captor
    private ArgumentCaptor<Meta> metaCaptor;

    @Test
    @DisplayName("Deve cadastrar receita diaria corretamente")
    void deveCadastrarReceitaDiariaCorretamente(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        if(request.getDataReceita().getDayOfWeek().equals(MONDAY)){
            request.setDataReceita(now().plusDays(1));
        }

        Usuario usuario = usuario();

        when(usuarioRepository.findById(request.getIdUsuario())).thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());
        verify(usuarioRepository).findById(request.getIdUsuario());
        verify(metaRepository, times(3)).save(metaCaptor.capture());
        verify(receitaDiariaRepository).save(receitaDiariaCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        ReceitaDiaria receitaDiariaResponse = receitaDiariaCaptor.getValue();

        for(int i = 0; i < metasResponse.size(); i++){

            double valorEsperadoMeta = usuario().getMetas().get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(request.getIdUsuario(), receitaDiariaResponse.getUsuario().getIdUsuario());
        assertEquals(request.getDataReceita(), receitaDiariaResponse.getDataReceita());
        assertEquals(request.getValor(), receitaDiariaResponse.getValor());
        assertEquals(usuario, receitaDiariaResponse.getUsuario());
        assertTrue(receitaDiariaResponse.isAtivo());
    }

    @Test
    @DisplayName("Deve cadastrar receita diaria corretamente se receita for no inicio da semana")
    void deveCadastrarReceitaDiariaCorretamenteSeReceitaForInicioSemana(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();
        request.setDataReceita(now().with(previousOrSame(MONDAY)));

        Usuario usuario = usuario();

        when(usuarioRepository.findById(request.getIdUsuario())).thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());
        verify(usuarioRepository).findById(request.getIdUsuario());
        if(request.getDataReceita().equals(now())){
            verify(metaRepository, times(3)).save(metaCaptor.capture());
        }
        else{
            verify(metaRepository, times(2)).save(metaCaptor.capture());
        }
        verify(receitaDiariaRepository).save(receitaDiariaCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        ReceitaDiaria receitaDiariaResponse = receitaDiariaCaptor.getValue();

        for(int i = 0; i < metasResponse.size(); i++){

            double valorEsperadoMeta = usuario().getMetas().get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(request.getIdUsuario(), receitaDiariaResponse.getUsuario().getIdUsuario());
        assertEquals(request.getDataReceita(), receitaDiariaResponse.getDataReceita());
        assertEquals(request.getValor(), receitaDiariaResponse.getValor());
        assertEquals(usuario, receitaDiariaResponse.getUsuario());
        assertTrue(receitaDiariaResponse.isAtivo());
    }

    @Test
    @DisplayName("Deve cadastrar receita diaria corretamente se receita for fora da semana")
    void deveCadastrarReceitaDiariaCorretamenteSeReceitaForForaSemana(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();
        request.setDataReceita(now().minusWeeks(1));

        Usuario usuario = usuario();

        when(usuarioRepository.findById(request.getIdUsuario())).thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());
        verify(usuarioRepository).findById(request.getIdUsuario());
        if(now().getMonth() == request.getDataReceita().getMonth() && now().getYear() == request.getDataReceita().getYear()){
            verify(metaRepository, times(1)).save(metaCaptor.capture());
        }
        else{
            verify(metaRepository, never()).save(metaCaptor.capture());
        }
        verify(receitaDiariaRepository).save(receitaDiariaCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        ReceitaDiaria receitaDiariaResponse = receitaDiariaCaptor.getValue();

        for(int i = 0; i < metasResponse.size(); i++){

            double valorEsperadoMeta = usuario().getMetas().get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(request.getIdUsuario(), receitaDiariaResponse.getUsuario().getIdUsuario());
        assertEquals(request.getDataReceita(), receitaDiariaResponse.getDataReceita());
        assertEquals(request.getValor(), receitaDiariaResponse.getValor());
        assertEquals(usuario, receitaDiariaResponse.getUsuario());
        assertTrue(receitaDiariaResponse.isAtivo());
    }

    @Test
    @DisplayName("Não deve cadastrar receita diaria corretamente se receita for fora do mes")
    void naoDeveCadastrarReceitaDiariaCorretamenteSeReceitaForForaMes(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();
        request.setDataReceita(now().minusMonths(2));

        Usuario usuario = usuario();

        when(usuarioRepository.findById(request.getIdUsuario())).thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());
        verify(usuarioRepository).findById(request.getIdUsuario());
        verify(metaRepository, never()).save(metaCaptor.capture());
        verify(receitaDiariaRepository).save(receitaDiariaCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        ReceitaDiaria receitaDiariaResponse = receitaDiariaCaptor.getValue();

        for(int i = 0; i < metasResponse.size(); i++){

            double valorEsperadoMeta = usuario().getMetas().get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(request.getIdUsuario(), receitaDiariaResponse.getUsuario().getIdUsuario());
        assertEquals(request.getDataReceita(), receitaDiariaResponse.getDataReceita());
        assertEquals(request.getValor(), receitaDiariaResponse.getValor());
        assertEquals(usuario, receitaDiariaResponse.getUsuario());
        assertTrue(receitaDiariaResponse.isAtivo());
    }

    @Test
    @DisplayName("Não deve cadastrar receita diaria corretamente se receita for fora do ano")
    void naoDeveCadastrarReceitaDiariaCorretamenteSeReceitaForForaAno(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();
        request.setDataReceita(now().minusYears(1));

        Usuario usuario = usuario();

        when(usuarioRepository.findById(request.getIdUsuario())).thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());
        verify(usuarioRepository).findById(request.getIdUsuario());
        verify(metaRepository, never()).save(metaCaptor.capture());
        verify(receitaDiariaRepository).save(receitaDiariaCaptor.capture());

        List<Meta> metasResponse = metaCaptor.getAllValues();

        ReceitaDiaria receitaDiariaResponse = receitaDiariaCaptor.getValue();

        for(int i = 0; i < metasResponse.size(); i++){

            double valorEsperadoMeta = usuario().getMetas().get(i).getValorAtual() + request.getValor();

            assertEquals(valorEsperadoMeta, metasResponse.get(i).getValorAtual());
        }

        assertEquals(request.getIdUsuario(), receitaDiariaResponse.getUsuario().getIdUsuario());
        assertEquals(request.getDataReceita(), receitaDiariaResponse.getDataReceita());
        assertEquals(request.getValor(), receitaDiariaResponse.getValor());
        assertEquals(usuario, receitaDiariaResponse.getUsuario());
        assertTrue(receitaDiariaResponse.isAtivo());
    }

    @Test
    @DisplayName("Deve cadastrar receita diaria corretamente sem registrar em metas")
    void deveCadastrarReceitaDiariaCorretamenteSemRegistrarMetas(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        Usuario usuario = usuario();
        usuario.setMetas(new ArrayList<>());

        when(usuarioRepository.findById(request.getIdUsuario())).thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());
        verify(usuarioRepository).findById(request.getIdUsuario());
        verify(metaRepository, never()).save(any(Meta.class));
        verify(receitaDiariaRepository).save(receitaDiariaCaptor.capture());

        ReceitaDiaria receitaDiariaResponse = receitaDiariaCaptor.getValue();

        assertEquals(request.getIdUsuario(), receitaDiariaResponse.getUsuario().getIdUsuario());
        assertEquals(request.getDataReceita(), receitaDiariaResponse.getDataReceita());
        assertEquals(request.getValor(), receitaDiariaResponse.getValor());
        assertEquals(usuario, receitaDiariaResponse.getUsuario());
        assertTrue(receitaDiariaResponse.isAtivo());
    }

    @Test
    @DisplayName("Não deve cadastrar a receita diaria se id do usuario incorreto")
    void naoDeveCadastrarReceitaDiariaSeIdUsuarioIncorreto(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        request.setIdUsuario(5L);

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(request.getIdUsuario());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator, never()).isPositivo(any(Double.class));
        verify(validaDataReceitaDiariaValidator, never()).naoMaiorQueHoje(any(LocalDate.class));
        verify(usuarioRepository, never()).findById(any(Long.class));
        verify(metaRepository, never()).save(any(Meta.class));
        verify(receitaDiariaRepository, never()).save(any(ReceitaDiaria.class));
    }

    @Test
    @DisplayName("Não deve cadastrar a receita diaria se valor da receita nao positivo")
    void naoDeveCadastrarReceitaDiariaSeValorNaoPositivo(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        request.setValor(-22.00);

        doThrow(ResponseStatusException.class).when(validaValorReceitaDiariaValidator).isPositivo(request.getValor());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator, never()).naoMaiorQueHoje(any(LocalDate.class));
        verify(usuarioRepository, never()).findById(any(Long.class));
        verify(metaRepository, never()).save(any(Meta.class));
        verify(receitaDiariaRepository, never()).save(any(ReceitaDiaria.class));
    }

    @Test
    @DisplayName("Não deve cadastrar a receita diaria se data maior que hoje")
    void naoDeveCadastrarReceitaDiariaSeDataMaiorQueHoje(){

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        request.setDataReceita(now().plusDays(1));

        doThrow(ResponseStatusException.class).when(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());
        verify(usuarioRepository, never()).findById(any(Long.class));
        verify(metaRepository, never()).save(any(Meta.class));
        verify(receitaDiariaRepository, never()).save(any(ReceitaDiaria.class));
    }
}
