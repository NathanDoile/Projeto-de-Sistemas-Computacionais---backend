package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InformacoesDaSemanaResponse {

    private double ganhoBruto;

    private double despesaTotal;

    private double lucroLiquido;

    private double kmAtual;

    private double segunda;

    private double terca;

    private double quarta;

    private double quinta;

    private double sexta;

    private double sabado;

    private double domingo;

}