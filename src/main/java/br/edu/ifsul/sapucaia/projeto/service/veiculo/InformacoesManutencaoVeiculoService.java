package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.InformacoesManutencaoVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao.*;

@Service
@RequiredArgsConstructor
public class InformacoesManutencaoVeiculoService {

    private final ValidaVeiculoService validaVeiculoService;

    private final VeiculoRepository veiculoRepository;

    private final ManutencaoRepository manutencaoRepository;

    @Transactional
    public InformacoesManutencaoVeiculoResponse buscarInformacoesManutencao(Long idVeiculo) {

        validaVeiculoService.porId(idVeiculo);

        List<Manutencao> manutencoes = manutencaoRepository.findAllByVeiculoIdVeiculoAndIsAtivo(idVeiculo, true);

        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(idVeiculo, true);

        List<Manutencao> manutencoesPreventivas = manutencoes
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(PREVENTIVA))
                .toList();

        List<Manutencao> manutencoesCorretivas = manutencoes
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(CORRETIVA))
                .toList();

        List<Manutencao> manutencoesPreditivas = manutencoes
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(PREDITIVA))
                .toList();

        int manutencoesPreventivasRealizadas = manutencoesPreventivas.size();
        int manutencoesCorretivasRealizadas = manutencoesCorretivas.size();
        int manutencoesPreditivasRealizadas = manutencoesPreditivas.size();

        double valorTotalPreventivas = manutencoesPreventivas
                .stream()
                .mapToDouble(manutencao -> manutencao.getCusto().getValor())
                .sum();

        double valorTotalCorretivas = manutencoesCorretivas
                .stream()
                .mapToDouble(manutencao -> manutencao.getCusto().getValor())
                .sum();

        double valorTotalPreditivas = manutencoesPreditivas
                .stream()
                .mapToDouble(manutencao -> manutencao.getCusto().getValor())
                .sum();

        double somaValoresManutencao = valorTotalPreventivas + valorTotalCorretivas + valorTotalPreditivas;

        int manutencoesComCusto = manutencoes.size();

        double mediaPrecoManutencao = somaValoresManutencao / manutencoesComCusto;

        double totalCustos = veiculo.getCustos() == null ? 0.00 :
                veiculo.getCustos()
                .stream()
                .filter(Custo::isAtivo)
                .mapToDouble(Custo::getValor)
                .sum();

        double valorCustoPorKmRodado = veiculo.getKmAtual() > 0
                ? totalCustos / veiculo.getKmAtual()
                : 0.00;

        return InformacoesManutencaoVeiculoResponse.builder()
                .totalManutencoesPreventivas(manutencoesPreventivasRealizadas)
                .totalManutencoesCorretivas(manutencoesCorretivasRealizadas)
                .totalManutencoesPreditivas(manutencoesPreditivasRealizadas)
                .valorTotalPreventivas(valorTotalPreventivas)
                .valorTotalCorretivas(valorTotalCorretivas)
                .valorTotalPreditivas(valorTotalPreditivas)
                .mediaPrecoManutencao(mediaPrecoManutencao)
                .valorCustoPorKmRodado(valorCustoPorKmRodado)
                .build();

        }
}