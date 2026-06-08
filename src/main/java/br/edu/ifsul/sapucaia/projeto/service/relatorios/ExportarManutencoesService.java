package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ExportarManutencoesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.mapper.ManutencaoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao.*;
import static org.springframework.util.ResourceUtils.getFile;

@Service
@RequiredArgsConstructor
public class ExportarManutencoesService {

    private final ManutencaoRepository manutencaoRepository;

    private final ValidaVeiculoService validaVeiculoService;

    private final PeriodoDataHelper periodoDataHelper;

    private final ValidaTipoPeriodoValidator validaTipoPeriodoValidator;

    public byte[] exportar(Long idVeiculo, String tipoPeriodo, String dataReferencia) throws FileNotFoundException, JRException {

        validaVeiculoService.porId(idVeiculo);
        validaTipoPeriodoValidator.porTipo(tipoPeriodo);

        PeriodoData periodoData = periodoDataHelper.calcularData(tipoPeriodo, dataReferencia);

        List<Manutencao> manutencoes = manutencaoRepository.findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween
                (idVeiculo, true, periodoData.dataInicio(), periodoData.dataFim());

        int quantidadePreventivas = manutencoes
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(PREVENTIVA))
                .toList()
                .size();

        int quantidadeCorretiva = manutencoes
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(CORRETIVA))
                .toList()
                .size();

        int quantidadePreditiva = manutencoes
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(PREDITIVA))
                .toList()
                .size();

        double total = manutencoes
                .stream()
                .mapToDouble(manutencao -> manutencao.getCusto().getValor())
                .sum();

        List<ExportarManutencoesResponse> manutencoesResponses = manutencoes
                .stream()
                .map(ManutencaoMapper::toResponse)
                .toList();

        File file = getFile("classpath:manutencoes.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource  dataSource = new JRBeanCollectionDataSource(manutencoesResponses);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("createdBy", "DriveX API");
        parametros.put("inicioPeriodo", periodoData.dataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        parametros.put("fimPeriodo", periodoData.dataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        parametros.put("quantidadePreventiva", quantidadePreventivas);
        parametros.put("quantidadeCorretiva", quantidadeCorretiva);
        parametros.put("quantidadePreditiva", quantidadePreditiva);
        parametros.put("total", total);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, "manutencoes.pdf");

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
