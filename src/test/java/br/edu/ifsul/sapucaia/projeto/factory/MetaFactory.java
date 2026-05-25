package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.BuscarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.*;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.RECEITA;

public class MetaFactory {

    public static CadastrarMetaRequest cadastrarMetaRequest() {

        return CadastrarMetaRequest
                .builder()
                .titulo("Meta Teste")
                .valor(200.00)
                .formato(SEMANAL.toString())
                .tipo(RECEITA.toString())
                .idUsuario(1L)
                .build();
    }

    public static BuscarMetaRequest buscarMetaRequest() {

        return BuscarMetaRequest
                .builder()
                .idUsuario(1L)
                .build();
    }

    public static Meta meta() {

        return Meta
                .builder()
                .idMeta(1L)
                .titulo("Salario")
                .formato(MENSAL)
                .tipo(RECEITA)
                .valorDesejado(3000)
                .valorAtual(0)
                .isAtivo(true)
                .build();
    }

    public static Meta metaMensal() {

        return Meta
                .builder()
                .idMeta(1L)
                .titulo("Salario")
                .formato(MENSAL)
                .tipo(RECEITA)
                .valorDesejado(3000)
                .valorAtual(0)
                .isAtivo(true)
                .build();
    }

    public static Meta metaSemanal() {

        return Meta
                .builder()
                .idMeta(1L)
                .titulo("Salario")
                .formato(SEMANAL)
                .tipo(RECEITA)
                .valorDesejado(3000)
                .valorAtual(0)
                .isAtivo(true)
                .build();
    }

    public static Meta metaDiaria() {

        return Meta
                .builder()
                .idMeta(1L)
                .titulo("Salario")
                .formato(DIARIA)
                .tipo(RECEITA)
                .valorDesejado(3000)
                .valorAtual(0)
                .isAtivo(true)
                .build();
    }
}