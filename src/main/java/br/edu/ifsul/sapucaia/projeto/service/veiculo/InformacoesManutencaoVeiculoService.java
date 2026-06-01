package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.InformacoesManutencaoVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
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

        List<Manutencao> manutencaos = manutencaoRepository.findAllByVeiculoIdVeiculoAndIsAtivo(idVeiculo, true);

        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(idVeiculo, true);

        List<Manutencao> manutencaosPreventivas = manutencaos
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(PREVENTIVA))
                .toList();

        List<Manutencao> manutencaosCorretivas = manutencaos
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(CORRETIVA))
                .toList();

        List<Manutencao> manutencaosPreditivas = manutencaos
                .stream()
                .filter(manutencao -> manutencao.getTipo().equals(PREDITIVA))
                .toList();

        int manutencoesPreventivas = manutencaosPreventivas.size();

        int manutencoesCorretivas = manutencaosCorretivas.size();

        int manutencoesPreditivas = manutencaosPreditivas.size();

        double valorTotalPreventivas = manutencaosPreventivas
                .stream()
                .mapToDouble(manutencao -> manutencao.getCusto().getValor())
                .sum();

        double valorTotalCorretivas = manutencaosCorretivas
                .stream()
                .mapToDouble(manutencao -> manutencao.getCusto().getValor())
                .sum();

        double valorTotalPreditivas = manutencaosPreditivas
                .stream()
                .mapToDouble(manutencao -> manutencao.getCusto().getValor())
                .sum();

        double somaValoresManutencao = valorTotalPreventivas + valorTotalCorretivas + valorTotalPreditivas;

        int manutencoesComCusto = manutencaos.size();

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
                .totalManutencoesPreventivas(manutencoesPreventivas)
                .totalManutencoesCorretivas(manutencoesCorretivas)
                .totalManutencoesPreditivas(manutencoesPreditivas)
                .valorTotalPreventivas(valorTotalPreventivas)
                .valorTotalCorretivas(valorTotalCorretivas)
                .valorTotalPreditivas(valorTotalPreditivas)
                .mediaPrecoManutencao(mediaPrecoManutencao)
                .valorCustoPorKmRodado(valorCustoPorKmRodado)
                .build();
    }
}