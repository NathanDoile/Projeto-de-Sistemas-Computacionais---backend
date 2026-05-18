package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaDoMesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import org.springframework.web.server.ResponseStatusException;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;

@Service
@RequiredArgsConstructor
public class GastosPorCategoriaDoPeriodoService {

    private final ValidaUsuarioService validaUsuarioService;
    private final CustoRepository custoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ValidaTipoPeriodoValidator validaTipoPeriodoValidator;

    public GastosPorCategoriaDoMesResponse calcularPorPeriodo(
            Long idUsuario,
            String tipo,
            String dataBase
    ) {

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

        Usuario usuario = usuarioRepository
                .findByIdUsuarioAndIsAtivo(idUsuario, true)
                .orElseThrow();

        List<Custo> custos = custoRepository
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(
                        usuario.getVeiculo().getIdVeiculo(),
                        inicio,
                        fim
                );

        Map<TipoCusto, Double> gastosMap = custos.stream()
                .collect(Collectors.groupingBy(
                        Custo::getTipo,
                        Collectors.summingDouble(Custo::getValor)
                ));

        return GastosPorCategoriaDoMesResponse.builder()
                .manutencao(gastosMap.getOrDefault(TipoCusto.MANUTENCAO, 0.0))
                .combustivel(gastosMap.getOrDefault(TipoCusto.COMBUSTIVEL, 0.0))
                .seguro(gastosMap.getOrDefault(TipoCusto.SEGURO, 0.0))
                .impostos(gastosMap.getOrDefault(TipoCusto.IMPOSTOS, 0.0))
                .outros(gastosMap.getOrDefault(TipoCusto.OUTROS, 0.0))
                .build();
    }

    private List<LocalDate> calcularPorDia(LocalDate dia) {
        return List.of(dia, dia);
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