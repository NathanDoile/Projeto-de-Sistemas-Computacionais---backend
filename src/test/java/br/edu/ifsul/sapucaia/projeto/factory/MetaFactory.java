package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.SEMANAL;

public class MetaFactory {

    public static CadastrarMetaRequest cadastrarMetaRequest() {

        return CadastrarMetaRequest
                .builder()
                .titulo("Meta Teste")
                .valor(200.00)
                .formato(SEMANAL.toString())
                .idUsuario(1L)
                .build();
    }
}
