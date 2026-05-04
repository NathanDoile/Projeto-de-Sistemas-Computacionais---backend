package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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

@Service
@RequiredArgsConstructor
public class GastosPorCategoriaDoPeriodoService {

    private final ValidaUsuarioService validaUsuarioService;
    private final CustoRepository custoRepository;
    private final UsuarioRepository usuarioRepository;

    public GastosPorCategoriaDoMesResponse calcularPorPeriodo(
            Long idUsuario,
            LocalDate inicio,
            LocalDate fim
    ) {

        validaUsuarioService.porId(idUsuario);

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

    public GastosPorCategoriaDoMesResponse calcularPorDia(Long idUsuario, LocalDate dia) {
        return calcularPorPeriodo(idUsuario, dia, dia);
    }

    public GastosPorCategoriaDoMesResponse calcularPorSemana(Long idUsuario, LocalDate dataBase) {
        LocalDate inicioSemana = dataBase.with(DayOfWeek.MONDAY);
        LocalDate fimSemana = dataBase.with(DayOfWeek.SUNDAY);

        return calcularPorPeriodo(idUsuario, inicioSemana, fimSemana);
    }

    public GastosPorCategoriaDoMesResponse calcularPorMes(Long idUsuario, LocalDate dataBase) {
        LocalDate inicioMes = dataBase.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimMes = dataBase.with(TemporalAdjusters.lastDayOfMonth());

        return calcularPorPeriodo(idUsuario, inicioMes, fimMes);
    }
}