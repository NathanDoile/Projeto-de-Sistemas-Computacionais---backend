package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinhaRelatorioFinanceiroResponse {
    private String label;
    private Double receita;
    private String tipoCusto;
    private Double valorCusto;
    private Double lucro;
}
