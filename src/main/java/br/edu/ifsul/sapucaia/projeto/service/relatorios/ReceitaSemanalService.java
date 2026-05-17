package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
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
    private final ValidaUsuarioService validaUsuarioService;
    private final UsuarioRepository usuarioRepository;

    public InformacoesDaSemanaResponse buscarReceitaDaSemana(Long idUsuario) {

        validaUsuarioService.porId(idUsuario);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true).get();

        LocalDate inicioSemana = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(MONDAY));

        LocalDate fimSemana = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(SUNDAY));

        List<ReceitaDiaria> receitas = receitaDiariaRepository
                .findByUsuarioIdUsuarioAndDataReceitaBetween(
                        usuario.getIdUsuario(),
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