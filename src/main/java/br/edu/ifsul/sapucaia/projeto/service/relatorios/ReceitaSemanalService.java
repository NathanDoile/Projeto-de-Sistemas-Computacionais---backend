package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumMap;
import java.util.List;

import static java.time.DayOfWeek.*;

@Service
@RequiredArgsConstructor
public class ReceitaSemanalService {

    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public InformacoesDaSemanaResponse buscarReceitaDaSemana() {

        UsuarioSecurity usuario = usuarioAutenticadoService.getUser();

        LocalDate inicioSemana = DateNow.now()
                .with(TemporalAdjusters.previousOrSame(MONDAY));

        LocalDate fimSemana = DateNow.now()
                .with(TemporalAdjusters.nextOrSame(SUNDAY));

        List<ReceitaDiaria> receitas = receitaDiariaRepository
                .findByUsuarioIdUsuarioAndDataReceitaBetween(
                        usuario.getId(),
                        inicioSemana,
                        fimSemana
                );

        double ganhoBruto = 0;

        EnumMap<DayOfWeek, Double> diasDaSemana = new EnumMap<>(DayOfWeek.class);

        for(DayOfWeek diaDaSemama : DayOfWeek.values()){
            diasDaSemana.put(diaDaSemama, 0.0);
        }

        for (ReceitaDiaria r : receitas) {

            double valor = r.getValor();
            ganhoBruto += valor;

            DayOfWeek diaDaSemana = r.getDataReceita().getDayOfWeek();

            Double novoValor = diasDaSemana.get(diaDaSemana) + valor;

            diasDaSemana.put(diaDaSemana, novoValor);
        }

        return InformacoesDaSemanaResponse.builder()
                .ganhoBruto(ganhoBruto)
                .segunda(diasDaSemana.get(MONDAY))
                .terca(diasDaSemana.get(TUESDAY))
                .quarta(diasDaSemana.get(WEDNESDAY))
                .quinta(diasDaSemana.get(THURSDAY))
                .sexta(diasDaSemana.get(FRIDAY))
                .sabado(diasDaSemana.get(SATURDAY))
                .domingo(diasDaSemana.get(SUNDAY))
                .build();
    }
}