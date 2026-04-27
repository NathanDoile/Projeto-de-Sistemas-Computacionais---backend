package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceitaSemanalService {

    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final ValidaUsuarioService validaUsuarioService;

    public InformacoesDaSemanaResponse buscarReceitaDaSemana(Long idUsuario) {

        Usuario usuario = validaUsuarioService.buscarUsuarioPorId(idUsuario);

        LocalDate inicioSemana = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate fimSemana = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<ReceitaDiaria> receitas = receitaDiariaRepository
                .findByUsuarioAndDataBetween(usuario, inicioSemana, fimSemana);

        double ganhoBruto = 0;

        double segunda = 0;
        double terca = 0;
        double quarta = 0;
        double quinta = 0;
        double sexta = 0;
        double sabado = 0;
        double domingo = 0;

        for (ReceitaDiaria r : receitas) {

            double valor = r.getValor();
            ganhoBruto += valor;

            LocalDate data = r.getDataReceita();
            if (data == null) continue;

            switch (data.getDayOfWeek()) {
                case MONDAY -> segunda += valor;
                case TUESDAY -> terca += valor;
                case WEDNESDAY -> quarta += valor;
                case THURSDAY -> quinta += valor;
                case FRIDAY -> sexta += valor;
                case SATURDAY -> sabado += valor;
                case SUNDAY -> domingo += valor;
            }
        }

        double despesaTotal = 0;

        double lucroLiquido = ganhoBruto - despesaTotal;

        double kmAtual = 0;
        return InformacoesDaSemanaResponse.builder()
                .ganhoBruto(ganhoBruto)
                .despesaTotal(despesaTotal)
                .lucroLiquido(lucroLiquido)
                .kmAtual(kmAtual)

                .segunda(segunda)
                .terca(terca)
                .quarta(quarta)
                .quinta(quinta)
                .sexta(sexta)
                .sabado(sabado)
                .domingo(domingo)

                .build();
    }
}