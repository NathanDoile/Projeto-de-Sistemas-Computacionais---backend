package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.InformacoesManutencaoVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InformacoesManutencaoVeiculoService {

    private final ValidaVeiculoService validaVeiculoService;

    private final VeiculoRepository veiculoRepository;

    @Transactional
    public InformacoesManutencaoVeiculoResponse buscarInformacoesManutencao(Long idVeiculo) {

        validaVeiculoService.porId(idVeiculo);
        validaVeiculoService.estaAtivo(idVeiculo);

        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(idVeiculo, true);

        int manutencoesPreventivas = 0;
        int manutencoesCorretivas = 0;
        int manutencoesPreditivas = 0;
        double valorTotalPreventivas = 0.0;
        double valorTotalCorretivas = 0.0;
        double valorTotalPreditivas = 0.0;
        double somaValoresManutencao = 0.0;
        int manutencoesComCusto = 0;

        List<Manutencao> manutencoes = veiculo.getManutencoes();
        if (manutencoes != null) {
            for (Manutencao manutencao : manutencoes) {
                if (!manutencao.isAtivo()) {
                    continue;
                }

                Custo custo = manutencao.getCusto();
                double valorCusto = 0.0;
                if (custo != null && custo.isAtivo()) {
                    valorCusto = custo.getValor();
                }

                if (manutencao.getTipo() != null) {
                    switch (manutencao.getTipo()) {
                        case PREVENTIVA -> {
                            manutencoesPreventivas++;
                            valorTotalPreventivas += valorCusto;
                        }
                        case CORRETIVA -> {
                            manutencoesCorretivas++;
                            valorTotalCorretivas += valorCusto;
                        }
                        case PREDITIVA -> {
                            manutencoesPreditivas++;
                            valorTotalPreditivas += valorCusto;
                        }
                    }
                }

                if (valorCusto > 0) {
                    manutencoesComCusto++;
                    somaValoresManutencao += valorCusto;
                }
            }
        }

        double mediaPrecoManutencao = 0.0;
        if (manutencoesComCusto > 0) {
            mediaPrecoManutencao = somaValoresManutencao / manutencoesComCusto;
        }

        double totalCustos = 0.0;
        List<Custo> custos = veiculo.getCustos();
        if (custos != null) {
            for (Custo custo : custos) {
                if (custo.isAtivo()) {
                    totalCustos += custo.getValor();
                }
            }
        }

        double valorCustoPorKmRodado = 0.0;
        if (veiculo.getKmAtual() > 0) {
            valorCustoPorKmRodado = totalCustos / veiculo.getKmAtual();
        }

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