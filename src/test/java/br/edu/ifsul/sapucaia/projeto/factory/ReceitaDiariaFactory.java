package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;

public class ReceitaDiariaFactory {

    public static CadastrarReceitaDiariaRequest cadastrarReceitaDiariaRequest() {

        return CadastrarReceitaDiariaRequest
                .builder()
                .dataReceita(DateNow.now())
                .idUsuario(usuario().getIdUsuario())
                .valor(200.00)
                .build();
    }

    public static ReceitaDiaria receitaDiaria() {

        return ReceitaDiaria
                .builder()
                .idReceita(1L)
                .dataReceita(DateNow.now())
                .valor(200.00)
                .isAtivo(true)
                .build();
    }
}
