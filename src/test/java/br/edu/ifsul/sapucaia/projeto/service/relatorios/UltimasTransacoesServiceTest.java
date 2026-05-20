package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.UltimasTransacoesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
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

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.receitaDiaria;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UltimasTransacoesServiceTest {

    @InjectMocks
    private UltimasTransacoesService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar ultimas transacoes corretamente")
    void deveRetornarUltimasTransacoesCorretamente(){

        Usuario usuario = usuario();

        Long idUsuario = usuario.getIdUsuario();
        Long idVeiculo = usuario.getVeiculo().getIdVeiculo();

        Custo custo = custo();

        ReceitaDiaria receita = receitaDiaria();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true))
                .thenReturn(java.util.Optional.of(usuario));

        when(custoRepository.findByVeiculoIdVeiculoAndIsAtivo(idVeiculo, true))
                .thenReturn(List.of(custo));

        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true))
                .thenReturn(List.of(receita));

        List<UltimasTransacoesResponse> response =
                tested.buscarUltimasTransacoes(idUsuario);

        verify(validaUsuarioService).porId(idUsuario);

        verify(usuarioRepository)
                .findByIdUsuarioAndIsAtivo(idUsuario, true);

        verify(custoRepository)
                .findByVeiculoIdVeiculoAndIsAtivo(idVeiculo, true);

        verify(receitaDiariaRepository)
                .findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true);

        assertEquals(2, response.size());

        boolean possuiCusto = response.stream()
                .anyMatch(transacao ->
                        transacao.getTipoTransacao().equals("CUSTO")
                                && transacao.getValor() == custo.getValor());

        boolean possuiReceita = response.stream()
                .anyMatch(transacao ->
                        transacao.getTipoTransacao().equals("RECEITA")
                                && transacao.getValor() == receita.getValor());

        assertTrue(possuiCusto);
        assertTrue(possuiReceita);
    }

    @Test
    @DisplayName("Nao deve retornar transacoes se id usuario incorreto")
    void naoDeveRetornarTransacoesSeIdUsuarioIncorreto(){

        Usuario usuario = usuario();

        Long idUsuario = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class)
                .when(validaUsuarioService)
                .porId(idUsuario);

        assertThrows(ResponseStatusException.class,
                () -> tested.buscarUltimasTransacoes(idUsuario));

        verify(validaUsuarioService).porId(idUsuario);

        verify(usuarioRepository, never())
                .findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));

        verify(custoRepository, never())
                .findByVeiculoIdVeiculoAndIsAtivo(any(Long.class), any(Boolean.class));

        verify(receitaDiariaRepository, never())
                .findByUsuarioIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
    }
}