package br.edu.ifsul.sapucaia.projeto.controller.response.veiculo;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InformacoesManutencaoVeiculoResponse {

    private int totalManutencoesPreventivas;
    private int totalManutencoesCorretivas;
    private int totalManutencoesPreditivas;
    private double valorTotalPreventivas;
    private double valorTotalCorretivas;
    private double valorTotalPreditivas;
    private double mediaPrecoManutencao;
    private double valorCustoPorKmRodado;
    
}
