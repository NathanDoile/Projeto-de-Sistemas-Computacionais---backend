package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.meta.BuscarMetaResponse;
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

    public static BuscarMetaResponse toResponse(Meta meta) {

        return BuscarMetaResponse
                .builder()
                .idMeta(meta.getIdMeta())
                .titulo(meta.getTitulo())
                .formato(meta.getFormato())
                .valorDesejado(meta.getValorDesejado())
                .valorAtual(meta.getValorAtual())
                .percentualAtingido((meta.getValorAtual()/ meta.getValorDesejado()) * 100)
                .build();
    }
}
