package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;
import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class GastosPorCategoriaResponse {

    private double manutencao;

    private double combustivel;

    private double seguro;

    private double impostos;

    private double outros;

    private double multas;
}