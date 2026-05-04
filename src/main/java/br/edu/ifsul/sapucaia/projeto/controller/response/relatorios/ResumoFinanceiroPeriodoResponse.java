package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumoFinanceiroPeriodoResponse {

    private double ganhoBruto;
    private double gastoTotal;
    private double lucroLiquido;
}