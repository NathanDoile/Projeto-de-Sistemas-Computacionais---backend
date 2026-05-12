package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static java.time.DayOfWeek.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaSemanalServiceTest {

    @InjectMocks
    private ReceitaSemanalService tested;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Test
    @DisplayName("Deve retornar a receita semanal corretamente")
    void deveRetornarReceitaSemanalCorretamente(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        List<ReceitaDiaria> receitas = usuario.getReceitasDiarias();

        when(validaUsuarioService.buscarUsuarioPorId(id)).thenReturn(usuario);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(eq(id), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(receitas);

        InformacoesDaSemanaResponse response = tested.buscarReceitaDaSemana(id);

        verify(validaUsuarioService).buscarUsuarioPorId(id);
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(eq(id), any(LocalDate.class), any(LocalDate.class));

        double ganhoBrutoEsperado = receitas
                .stream()
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        EnumMap<DayOfWeek, Double> valoresDaSemanaResponse = new EnumMap<>(DayOfWeek.class);
        valoresDaSemanaResponse.put(MONDAY, response.getSegunda());
        valoresDaSemanaResponse.put(TUESDAY, response.getTerca());
        valoresDaSemanaResponse.put(WEDNESDAY, response.getQuarta());
        valoresDaSemanaResponse.put(THURSDAY, response.getQuinta());
        valoresDaSemanaResponse.put(FRIDAY, response.getSexta());
        valoresDaSemanaResponse.put(SATURDAY, response.getSabado());
        valoresDaSemanaResponse.put(SUNDAY, response.getDomingo());

        valoresDaSemanaResponse.forEach((dia, valor) -> {

            double valorEsperado = receitas
                    .stream()
                    .filter(receita -> receita.getDataReceita().getDayOfWeek().equals(dia))
                    .mapToDouble(ReceitaDiaria::getValor)
                    .sum();

            assertEquals(valorEsperado, valor);
        });

        assertEquals(ganhoBrutoEsperado, response.getGanhoBruto());
    }

    @Test
    @DisplayName("Não deve retornar receita da semana se id do usuario incorreto")
    void naoDeveRetornarReceitaSemanaSeIdUsuarioIncorreto(){

        Usuario usuario = usuario();

        Long id = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class).when(validaUsuarioService).buscarUsuarioPorId(id);

        assertThrows(ResponseStatusException.class, () -> tested.buscarReceitaDaSemana(id));

        verify(validaUsuarioService).buscarUsuarioPorId(id);
        verify(receitaDiariaRepository, never()).findByUsuarioIdUsuarioAndDataReceitaBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }
}
