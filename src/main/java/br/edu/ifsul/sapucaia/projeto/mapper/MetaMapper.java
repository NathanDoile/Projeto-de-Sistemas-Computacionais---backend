package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;

public class MetaMapper {

    private MetaMapper(){}

    public static Meta toEntity(CadastrarMetaRequest cadastrarMetaRequest) {

        return Meta
                .builder()
                .titulo(cadastrarMetaRequest.getTitulo())
                .formato(FormatoMeta.deTexto(cadastrarMetaRequest.getFormato()))
                .valorDesejado(cadastrarMetaRequest.getValor())
                .build();
    }
    
}
