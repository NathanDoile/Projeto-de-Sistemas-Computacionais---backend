package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;

public class MetaMapper {

    private MetaMapper(){}

    public static Meta toEntity(CadastrarMetaRequest cadastrarMetaRequest) {

        return Meta
                .builder()
                .titulo(cadastrarMetaRequest.getTitulo())
                .formato(cadastrarMetaRequest.getFormato())
                .valor(cadastrarMetaRequest.getValor())
                .build();
    }
    
}
