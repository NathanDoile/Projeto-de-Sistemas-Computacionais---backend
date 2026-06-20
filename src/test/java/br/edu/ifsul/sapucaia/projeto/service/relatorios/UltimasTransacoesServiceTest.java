package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.UltimasTransacoesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.receitaDiaria;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UltimasTransacoesServiceTest {

    @InjectMocks
    private UltimasTransacoesService tested;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Test
    @DisplayName("Deve retornar ultimas transacoes corretamente")
    void deveRetornarUltimasTransacoesCorretamente(){

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Long idUsuario = usuarioSecurity.getId();
        Long idVeiculo = usuarioSecurity.getIdVeiculo();

        Custo custo = custo();

        ReceitaDiaria receita = receitaDiaria();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        when(custoRepository.findByVeiculoIdVeiculo(idVeiculo))
                .thenReturn(List.of(custo));

        when(receitaDiariaRepository.findByUsuarioIdUsuario(idUsuario))
                .thenReturn(List.of(receita));

        List<UltimasTransacoesResponse> response =
                tested.buscarUltimasTransacoes();

        verify(usuarioAutenticadoService).getUser();

        verify(custoRepository)
                .findByVeiculoIdVeiculo(idVeiculo);

        verify(receitaDiariaRepository)
                .findByUsuarioIdUsuario(idUsuario);

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
}