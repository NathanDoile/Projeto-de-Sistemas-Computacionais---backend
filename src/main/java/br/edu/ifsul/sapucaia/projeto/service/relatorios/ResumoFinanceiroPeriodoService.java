package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class ResumoFinanceiroPeriodoService {

    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final CustoRepository custoRepository;
    private final ValidaUsuarioService validaUsuarioService;

    // 🔥 MÉTODO BASE
    public ResumoFinanceiroPeriodoResponse calcularPorPeriodo(
            Long idUsuario,
            LocalDate inicio,
            LocalDate fim
    ) {

        Usuario usuario = validaUsuarioService.buscarUsuarioPorId(idUsuario);
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

    // 📅 DIA
    public ResumoFinanceiroPeriodoResponse calcularPorDia(Long idUsuario, LocalDate dia) {
        return calcularPorPeriodo(idUsuario, dia, dia);
    }

    // 📅 SEMANA
    public ResumoFinanceiroPeriodoResponse calcularPorSemana(Long idUsuario, LocalDate dataBase) {
        LocalDate inicioSemana = dataBase.with(DayOfWeek.MONDAY);
        LocalDate fimSemana = dataBase.with(DayOfWeek.SUNDAY);

        return calcularPorPeriodo(idUsuario, inicioSemana, fimSemana);
    }

    // 📅 MÊS
    public ResumoFinanceiroPeriodoResponse calcularPorMes(Long idUsuario, LocalDate dataBase) {
        LocalDate inicioMes = dataBase.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimMes = dataBase.with(TemporalAdjusters.lastDayOfMonth());

        return calcularPorPeriodo(idUsuario, inicioMes, fimMes);
    }
}