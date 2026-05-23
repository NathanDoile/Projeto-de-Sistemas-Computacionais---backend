package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final ValidaTipoPeriodoValidator validaTipoPeriodoValidator;
    private final PeriodoDataHelper periodoDataHelper;

    public GastosPorCategoriaDoMesResponse calcularPorPeriodo(Long idUsuario, String tipo, String dataBase) {

        validaUsuarioService.porId(idUsuario);
        validaTipoPeriodoValidator.porTipo(tipo);

        PeriodoData periodoData = periodoDataHelper.calcularData(tipo, dataBase);

        LocalDate inicio = periodoData.dataInicio();
        LocalDate fim = periodoData.dataFim();

        Usuario usuario = usuarioRepository
                .findByIdUsuarioAndIsAtivo(idUsuario, true)
                .get();

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
                .multas(gastosMap.getOrDefault(TipoCusto.MULTAS, 0.0))
                .outros(gastosMap.getOrDefault(TipoCusto.OUTROS, 0.0))
                .build();
    }
}