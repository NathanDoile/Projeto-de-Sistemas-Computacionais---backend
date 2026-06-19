package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.receitaDiaria;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static java.time.DayOfWeek.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceitaSemanalServiceTest {

    @InjectMocks
    private ReceitaSemanalService tested;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Test
    @DisplayName("Deve retornar a receita semanal corretamente")
    void deveRetornarReceitaSemanalCorretamente(){

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        List<ReceitaDiaria> receitas = new ArrayList<>();

        LocalDate hoje = DateNow.now();

        receitas.add(receitaDiaria());

        for(int i = 1; i < 7; i++){
            ReceitaDiaria receita = receitaDiaria();
            receita.setDataReceita(hoje.minusDays(i));

            receitas.add(receita);
        }

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(eq(usuarioSecurity.getId()), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(receitas);

        InformacoesDaSemanaResponse response = tested.buscarReceitaDaSemana();

        verify(usuarioAutenticadoService).getUser();
        verify(receitaDiariaRepository).findByUsuarioIdUsuarioAndDataReceitaBetween(eq(usuarioSecurity.getId()), any(LocalDate.class), any(LocalDate.class));

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
}
