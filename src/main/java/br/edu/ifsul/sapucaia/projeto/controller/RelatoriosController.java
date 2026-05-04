package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse; // ✅ ADICIONA ISSO
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaDoMesResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.UltimasTransacoesResponse;
import br.edu.ifsul.sapucaia.projeto.service.relatorios.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatoriosController {

    private final InformacoesDaSemanaService informacoesDaSemanaService;
    private final ReceitaSemanalService receitaSemanaService;
    private final ResumoFinanceiroPeriodoService resumoFinanceiroService;
    private final GastosPorCategoriaDoPeriodoService gastosPorCategoriaService;
    private final UltimasTransacoesService ultimasTransacoesService;

    @GetMapping("/informacoes-semana/{idUsuario}")
    public InformacoesDaSemanaResponse getInformacoesSemana(@PathVariable Long idUsuario){
        return informacoesDaSemanaService.buscarInformacoesDaSemana(idUsuario);
    }

    @GetMapping("/receita-semana/{idUsuario}")
    public InformacoesDaSemanaResponse buscarReceitaDaSemana(@PathVariable Long idUsuario){
        return receitaSemanaService.buscarReceitaDaSemana(idUsuario);
    }

    @GetMapping("/resumo-financeiro/{idUsuario}")
    public ResumoFinanceiroPeriodoResponse getResumoFinanceiro(
            @PathVariable Long idUsuario,
            @RequestParam String tipo,
            @RequestParam(required = false) String data
    ) {

        LocalDate dataBase = (data != null)
                ? LocalDate.parse(data)
                : LocalDate.now();

        switch (tipo.toLowerCase()) {
            case "dia":
                return resumoFinanceiroService.calcularPorDia(idUsuario, dataBase);

            case "semana":
                return resumoFinanceiroService.calcularPorSemana(idUsuario, dataBase);

            case "mes":
                return resumoFinanceiroService.calcularPorMes(idUsuario, dataBase);

            default:
                throw new IllegalArgumentException("Tipo inválido");
        }
    }

    //DIA
    @GetMapping("/gastos-categoria-dia/{idUsuario}")
    public GastosPorCategoriaDoMesResponse getPorDia(
            @PathVariable Long idUsuario,
            @RequestParam String data
    ) {
        LocalDate dia = LocalDate.parse(data);
        return gastosPorCategoriaService.calcularPorDia(idUsuario, dia);
    }

    //SEMANA
    @GetMapping("/gastos-categoria-semana/{idUsuario}")
    public GastosPorCategoriaDoMesResponse getPorSemana(
            @PathVariable Long idUsuario,
            @RequestParam(required = false) String data
    ) {
        LocalDate dataBase = (data != null)
                ? LocalDate.parse(data)
                : LocalDate.now();

        return gastosPorCategoriaService.calcularPorSemana(idUsuario, dataBase);
    }

    //MÊS
    @GetMapping("/gastos-categoria-mes/{idUsuario}")
    public GastosPorCategoriaDoMesResponse getPorMes(
            @PathVariable Long idUsuario,
            @RequestParam(required = false) String data
    ) {
        LocalDate dataBase = (data != null)
                ? LocalDate.parse(data)
                : LocalDate.now();

        return gastosPorCategoriaService.calcularPorMes(idUsuario, dataBase);
    }

    @GetMapping("/ultimas-transacoes/{idUsuario}")
    public List<UltimasTransacoesResponse> getUltimasTransacoes(@PathVariable Long idUsuario) {
        return ultimasTransacoesService.buscarUltimasTransacoes(idUsuario);
    }
}