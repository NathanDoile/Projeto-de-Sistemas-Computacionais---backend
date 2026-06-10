package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.LinhaRelatorioFinanceiroResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.PeriodoRelatorioFinanceiro;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataRelatorioFinanceiroPdfValidator;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.PeriodoRelatorioFinanceiro.ANUAL;

@Service
@RequiredArgsConstructor
public class GerarRelatorioFinanceiroPdfService {

    private final ValidaUsuarioService validaUsuarioService;
    private final ValidaDataRelatorioFinanceiroPdfValidator validaDataRelatorioFinanceiroPdfValidator;
    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final CustoRepository custoRepository;
    private final VeiculoRepository veiculoRepository;

    public byte[] gerarRelatorioFinanceiro(Long idUsuario, LocalDate dataReferencia, PeriodoRelatorioFinanceiro periodo){
        validaUsuarioService.porId(idUsuario);
        validaDataRelatorioFinanceiroPdfValidator.naoMaiorQueHoje(dataReferencia);

        LocalDate dataInicio = dataReferencia;
        LocalDate dataFim = dataReferencia;

        switch (periodo) {
            case SEMANAL -> {
                dataInicio = dataReferencia.with(DayOfWeek.MONDAY);
                dataFim = dataReferencia.with(DayOfWeek.SUNDAY);
            }
            case MENSAL -> {
                dataInicio = dataReferencia.withDayOfMonth(1);
                dataFim = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());
            }
            case ANUAL -> {
                dataInicio = dataReferencia.withDayOfYear(1);
                dataFim = dataReferencia.withDayOfYear(dataReferencia.lengthOfYear());
            }
        }

        Veiculo veiculo = veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true);

        List<ReceitaDiaria> receitas = receitaDiariaRepository
                .findByUsuarioIdUsuarioAndDataReceitaBetween(idUsuario, dataInicio, dataFim);

        List<Custo> custos = custoRepository
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), dataInicio, dataFim);

        List<LinhaRelatorioFinanceiroResponse> linhas = periodo == ANUAL
                ? agruparPorMes(receitas, custos, dataInicio, dataFim)
                : agruparPorDia(receitas, custos, dataInicio, dataFim);

        return gerarPdf(veiculo.getUsuario().getNome(), periodo, dataInicio, dataFim, linhas);
    }

    private List<LinhaRelatorioFinanceiroResponse> agruparPorDia(List<ReceitaDiaria> receitas, List<Custo> custos, LocalDate dataInicio, LocalDate dataFim) {
        List<LinhaRelatorioFinanceiroResponse> linhas = new ArrayList<>();

        for (LocalDate dia = dataInicio; !dia.isAfter(dataFim); dia = dia.plusDays(1)) {
            LocalDate diaFinal = dia;

            double totalReceita = receitas.stream()
                    .filter(r -> r.getDataReceita().equals(diaFinal))
                    .mapToDouble(ReceitaDiaria::getValor).sum();

            List<Custo> custosDia = custos.stream()
                    .filter(c -> c.getDataPagamento().equals(diaFinal))
                    .toList();

            adicionarLinhas(linhas,
                    diaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    totalReceita, custosDia);
        }

        return linhas;
    }

    private List<LinhaRelatorioFinanceiroResponse> agruparPorMes(List<ReceitaDiaria> receitas, List<Custo> custos, LocalDate dataInicio, LocalDate dataFim) {
        List<LinhaRelatorioFinanceiroResponse> linhas = new ArrayList<>();

        for (LocalDate mes = dataInicio.withDayOfMonth(1); !mes.isAfter(dataFim); mes = mes.plusMonths(1)) {
            LocalDate inicioMes = mes;
            LocalDate fimMes = mes.withDayOfMonth(mes.lengthOfMonth());

            double totalReceita = receitas.stream()
                    .filter(r -> !r.getDataReceita().isBefore(inicioMes) && !r.getDataReceita().isAfter(fimMes))
                    .mapToDouble(ReceitaDiaria::getValor).sum();

            List<Custo> custosMes = custos.stream()
                    .filter(c -> !c.getDataPagamento().isBefore(inicioMes) && !c.getDataPagamento().isAfter(fimMes))
                    .toList();

            adicionarLinhas(linhas,
                    mes.format(DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR"))),
                    totalReceita, custosMes);
        }

        return linhas;
    }

    private void adicionarLinhas(List<LinhaRelatorioFinanceiroResponse> linhas, String label, double totalReceita, List<Custo> custosGrupo) {
        List<TipoCusto> tipos = Arrays.stream(TipoCusto.values()).toList();
        double totalCusto = custosGrupo.stream().mapToDouble(Custo::getValor).sum();
        double lucro = totalReceita - totalCusto;

        if (totalReceita == 0 && totalCusto == 0) return;

        List<TipoCusto> tiposComValor = tipos.stream()
                .filter(tipo -> custosGrupo.stream()
                        .filter(c -> c.getTipo() == tipo)
                        .mapToDouble(Custo::getValor).sum() > 0)
                .toList();

        if (tiposComValor.isEmpty()) {
            linhas.add(new LinhaRelatorioFinanceiroResponse(label, totalReceita, null, 0.0, lucro));
            return;
        }

        for (int i = 0; i < tiposComValor.size(); i++) {
            TipoCusto tipo = tiposComValor.get(i);
            double valorTipo = custosGrupo.stream()
                    .filter(c -> c.getTipo() == tipo)
                    .mapToDouble(Custo::getValor).sum();

            linhas.add(new LinhaRelatorioFinanceiroResponse(
                    i == 0 ? label : null,
                    i == 0 ? totalReceita : null,
                    tipo.getDescricao(),
                    valorTipo,
                    i == 0 ? lucro : null
            ));
        }
    }

    private byte[] gerarPdf(String nomeUsuario, PeriodoRelatorioFinanceiro periodo, LocalDate dataInicio, LocalDate dataFim, List<LinhaRelatorioFinanceiroResponse> linhas) {
        InputStream jasperStream = getClass()
                .getResourceAsStream("/templates/financeiro/Relatorio_Financeiro.jasper");

        double totalReceita = linhas.stream()
                .filter(l -> l.getReceita() != null)
                .mapToDouble(LinhaRelatorioFinanceiroResponse::getReceita).sum();

        double totalCusto = linhas.stream()
                .mapToDouble(LinhaRelatorioFinanceiroResponse::getValorCusto).sum();

        Map<String, Object> params = new HashMap<>();
        params.put("NOME_USUARIO", nomeUsuario);
        params.put("PERIODO", periodo.name());
        params.put("DATA_INICIO", dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        params.put("DATA_FIM", dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        params.put("TOTAL_RECEITA", totalReceita);
        params.put("TOTAL_CUSTO", totalCusto);
        params.put("LUCRO_GERAL", totalReceita - totalCusto);
        params.put("LINHAS", new JRBeanCollectionDataSource(linhas));

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint, "financeiro.pdf");

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Erro ao gerar relatório financeiro", e);
        }
    }
}