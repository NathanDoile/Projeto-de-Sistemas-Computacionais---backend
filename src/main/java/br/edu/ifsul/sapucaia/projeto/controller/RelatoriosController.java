package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaDoMesResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.UltimasTransacoesResponse;
import br.edu.ifsul.sapucaia.projeto.service.relatorios.*;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

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
            @RequestParam(required = false) String dataBase
    ) {
        return resumoFinanceiroService.calcularPorPeriodo(idUsuario, tipo, dataBase);
    }

    @GetMapping("/gastos-categoria/{idUsuario}")
    public GastosPorCategoriaDoMesResponse getGastosPorCategoria(
            @PathVariable Long idUsuario,
            @RequestParam String tipo,
            @RequestParam String dataBase
    ) {
        return gastosPorCategoriaService.calcularPorPeriodo(idUsuario, tipo, dataBase);
    }

    @GetMapping("/ultimas-transacoes/{idUsuario}")
    public List<UltimasTransacoesResponse> getUltimasTransacoes(@PathVariable Long idUsuario) {
        return ultimasTransacoesService.buscarUltimasTransacoes(idUsuario);
    }

    @GetMapping("/exportar/manutencoes")
    public byte[] exportarManutencoes(@RequestParam Long idVeiculo,
                                      @RequestParam String tipoPeriodo,
                                      @RequestParam String dataReferencia) throws JRException, FileNotFoundException {
        return exportarManutencoesService.exportar(idVeiculo, tipoPeriodo, dataReferencia);
    }

    @GetMapping("/exportar/financeiro")
    public ResponseEntity<byte[]> gerarRelatorio(
            @RequestParam Long idUsuario,
            @RequestParam LocalDate dataReferencia,
            @RequestParam String periodo){

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_financeiro.pdf")
                .body(gerarRelatorioFinanceiroPdfService.gerarRelatorioFinanceiro(idUsuario, dataReferencia, PeriodoRelatorioFinanceiro.deTexto(periodo)));
    }
}