package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;

@Service
@RequiredArgsConstructor
public class GastosPorCategoriaDoPeriodoService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final CustoRepository custoRepository;
    private final ValidaTipoPeriodoValidator validaTipoPeriodoValidator;
    private final PeriodoDataHelper periodoDataHelper;

    public GastosPorCategoriaResponse calcularPorPeriodo(String tipo, String dataBase) {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();
        validaTipoPeriodoValidator.porTipo(tipo);

        PeriodoData periodoData = periodoDataHelper.calcularData(tipo, dataBase);

        LocalDate inicio = periodoData.dataInicio();
        LocalDate fim = periodoData.dataFim();

        List<Custo> custos = custoRepository
                .findByVeiculoIdVeiculoAndDataPagamentoBetween(
                        usuarioSecurity.getIdVeiculo(),
                        inicio,
                        fim
                );

        Map<TipoCusto, Double> gastosMap = custos.stream()
                .collect(Collectors.groupingBy(
                        Custo::getTipo,
                        Collectors.summingDouble(Custo::getValor)
                ));

        return GastosPorCategoriaResponse.builder()
                .manutencao(gastosMap.getOrDefault(TipoCusto.MANUTENCAO, 0.0))
                .combustivel(gastosMap.getOrDefault(TipoCusto.COMBUSTIVEL, 0.0))
                .seguro(gastosMap.getOrDefault(TipoCusto.SEGURO, 0.0))
                .impostos(gastosMap.getOrDefault(TipoCusto.IMPOSTOS, 0.0))
                .multas(gastosMap.getOrDefault(TipoCusto.MULTAS, 0.0))
                .outros(gastosMap.getOrDefault(TipoCusto.OUTROS, 0.0))
                .build();
    }
}