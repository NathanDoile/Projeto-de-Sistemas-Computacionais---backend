package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;

import java.time.LocalDate;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static java.time.LocalDate.now;

public class ReceitaDiariaFactory {

    public static CadastrarReceitaDiariaRequest cadastrarReceitaDiariaRequest() {

        return CadastrarReceitaDiariaRequest
                .builder()
                .dataReceita(now())
                .idUsuario(usuario().getIdUsuario())
                .valor(200.00)
                .build();
    }

    public static ReceitaDiaria receitaDiaria() {

        return ReceitaDiaria
                .builder()
                .idReceita(1L)
                .dataReceita(now())
                .valor(200.00)
                .isAtivo(true)
                .usuario(usuario())
                .build();
    }
}
