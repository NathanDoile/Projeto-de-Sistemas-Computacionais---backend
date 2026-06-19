package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ResumoFinanceiroPeriodoService {

    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final CustoRepository custoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ValidaTipoPeriodoValidator validaTipoPeriodoValidator;
    private final PeriodoDataHelper periodoDataHelper;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public ResumoFinanceiroPeriodoResponse calcularPorPeriodo(String tipo, String dataBase) {

        validaTipoPeriodoValidator.porTipo(tipo);

        PeriodoData periodoData = periodoDataHelper.calcularData(tipo, dataBase);

        LocalDate inicio = periodoData.dataInicio();
        LocalDate fim = periodoData.dataFim();

        UsuarioSecurity usuario = usuarioAutenticadoService.getUser();
        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(usuario.getIdVeiculo(), true);

        double ganhoBruto = receitaDiariaRepository
                .findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getId(), inicio, fim)
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
}