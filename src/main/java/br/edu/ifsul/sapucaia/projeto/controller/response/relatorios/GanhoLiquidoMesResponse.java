package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GanhoLiquidoMesResponse {

    private double lucroLiquido;
}