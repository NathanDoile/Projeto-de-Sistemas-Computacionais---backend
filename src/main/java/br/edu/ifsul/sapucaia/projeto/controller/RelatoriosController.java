package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.PendenciasDoUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.UltimasTransacoesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.enums.PeriodoRelatorioFinanceiro;
import br.edu.ifsul.sapucaia.projeto.service.relatorios.*;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatoriosController {

    private final InformacoesDaSemanaService informacoesDaSemanaService;
    private final ReceitaSemanalService receitaSemanaService;
    private final ResumoFinanceiroPeriodoService resumoFinanceiroService;
    private final GastosPorCategoriaDoPeriodoService gastosPorCategoriaService;
    private final UltimasTransacoesService ultimasTransacoesService;
    private final GerarRelatorioFinanceiroPdfService gerarRelatorioFinanceiroPdfService;
    private final ExportarManutencoesService exportarManutencoesService;
    private final PendenciasDoUsuarioService pendenciasDoUsuarioService;

    @GetMapping("/informacoes-semana")
    public InformacoesDaSemanaResponse getInformacoesSemana() {
        return informacoesDaSemanaService.buscarInformacoesDaSemana();
    }

    @GetMapping("/receita-semana")
    public InformacoesDaSemanaResponse buscarReceitaDaSemana() {
        return receitaSemanaService.buscarReceitaDaSemana();
    }

    @GetMapping("/resumo-financeiro")
    public ResumoFinanceiroPeriodoResponse getResumoFinanceiro(
            @RequestParam String tipo,
            @RequestParam(required = false) String dataBase
    ) {
        return resumoFinanceiroService.calcularPorPeriodo(tipo, dataBase);
    }

    @GetMapping("/gastos-categoria")
    public GastosPorCategoriaResponse getGastosPorCategoria(
            @RequestParam String tipo,
            @RequestParam String dataBase
    ) {
        return gastosPorCategoriaService.calcularPorPeriodo(tipo, dataBase);
    }

    @GetMapping("/ultimas-transacoes")
    public List<UltimasTransacoesResponse> getUltimasTransacoes() {
        return ultimasTransacoesService.buscarUltimasTransacoes();
    }

    @GetMapping("/pendencias")
    public PendenciasDoUsuarioResponse getPendenciasDoUsuario() {
        return pendenciasDoUsuarioService.buscarPendencias();
    }

    @GetMapping("/exportar/manutencoes")
    public byte[] exportarManutencoes(
            @RequestParam String tipoPeriodo,
            @RequestParam String dataReferencia)
            throws JRException, FileNotFoundException {

        return exportarManutencoesService.exportar(
                tipoPeriodo,
                dataReferencia
        );
    }

    @GetMapping("/exportar/financeiro")
    public ResponseEntity<byte[]> gerarRelatorio(
            @RequestParam LocalDate dataReferencia,
            @RequestParam String periodo) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=relatorio_financeiro.pdf")
                .body(
                        gerarRelatorioFinanceiroPdfService.gerarRelatorioFinanceiro(
                                dataReferencia,
                                PeriodoRelatorioFinanceiro.deTexto(periodo)
                        )
                );
    }
}