package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ResumoFinanceiroPeriodoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.PeriodoDataHelper;
import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoPeriodoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ResumoFinanceiroPeriodoService {

    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final CustoRepository custoRepository;
    private final ValidaUsuarioService validaUsuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ValidaTipoPeriodoValidator validaTipoPeriodoValidator;
    private final PeriodoDataHelper periodoDataHelper;

    public ResumoFinanceiroPeriodoResponse calcularPorPeriodo(Long idUsuario, String tipo, String dataBase) {

        validaUsuarioService.porId(idUsuario);
        validaTipoPeriodoValidator.porTipo(tipo);

        PeriodoData periodoData = periodoDataHelper.calcularData(tipo, dataBase);

        LocalDate inicio = periodoData.dataInicio();
        LocalDate fim = periodoData.dataFim();

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true).get();
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
}