package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExportarManutencoesResponse {

    private final String tipo;

    private final String dataManutencao;

    private final String descricao;

    private final double valor;
}
