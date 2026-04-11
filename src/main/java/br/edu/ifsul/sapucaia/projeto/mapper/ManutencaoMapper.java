package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;

public class ManutencaoMapper {

    private ManutencaoMapper(){}


    public static Manutencao toEntity(CadastrarManutencaoRequest cadastrarManutencaoRequest) {

        return Manutencao
                .builder()
                .tipo(cadastrarManutencaoRequest.getTipo())
                .dataManutencao(cadastrarManutencaoRequest.getDataManutencao())
                .descricao(cadastrarManutencaoRequest.getDescricao())
                .build();
    }
}
