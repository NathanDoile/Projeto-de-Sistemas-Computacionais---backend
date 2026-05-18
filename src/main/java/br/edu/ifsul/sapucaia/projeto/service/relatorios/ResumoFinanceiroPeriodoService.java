package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;

@Service
@RequiredArgsConstructor
public class ResumoFinanceiroPeriodoService {

    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final CustoRepository custoRepository;
    private final ValidaUsuarioService validaUsuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ValidaTipoPeriodoValidator validaTipoPeriodoValidator;

    public ResumoFinanceiroPeriodoResponse calcularPorPeriodo(Long idUsuario, String tipo, String dataBase) {

        validaUsuarioService.porId(idUsuario);
        validaTipoPeriodoValidator.porTipo(tipo);

        LocalDate dataBaseDate = (dataBase != null)
                ? parse(dataBase)
                : now();

        List<LocalDate> datasInicioFim = new ArrayList<>();

        switch (tipo.toLowerCase()) {
            case "dia" -> datasInicioFim = calcularPorDia(dataBaseDate);

            case "semana" -> datasInicioFim = calcularPorSemana(dataBaseDate);

            case "mes" -> datasInicioFim = calcularPorMes(dataBaseDate);
        }

        LocalDate inicio = datasInicioFim.get(0);
        LocalDate fim = datasInicioFim.get(1);

            Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true).get();
            Veiculo veiculo = usuario.getVeiculo();

            double ganhoBruto = receitaDiariaRepository
                    .findByUsuarioIdUsuarioAndDataReceitaBetween(idUsuario, inicio, fim)
                    .stream()
                    .mapToDouble(ReceitaDiaria::getValor)
                    .sum();

            double gastoTotal = custoRepository
                    .findByVeiculoIdVeiculoAndDataPagamentoBetween(
                            veiculo.getIdVeiculo(), inicio, fim)
                    .stream()
                    .mapToDouble(Custo::getValor)
                    .sum();

            double lucroLiquido = ganhoBruto - gastoTotal;

            return ResumoFinanceiroPeriodoResponse.builder()
                    .ganhoBruto(ganhoBruto)
                    .gastoTotal(gastoTotal)
                    .lucroLiquido(lucroLiquido)
                    .build();

    }

    private List<LocalDate> calcularPorDia(LocalDate dataBase) {

        return List.of(dataBase, dataBase);
    }

    private List<LocalDate> calcularPorSemana(LocalDate dataBase) {
        LocalDate inicioSemana = dataBase.with(DayOfWeek.MONDAY);
        LocalDate fimSemana = dataBase.with(DayOfWeek.SUNDAY);

        return List.of(inicioSemana, fimSemana);
    }

    private List<LocalDate> calcularPorMes(LocalDate dataBase) {
        LocalDate inicioMes = dataBase.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimMes = dataBase.with(TemporalAdjusters.lastDayOfMonth());

        return List.of(inicioMes, fimMes);
    }
}