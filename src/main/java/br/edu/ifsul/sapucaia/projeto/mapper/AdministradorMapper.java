package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import jakarta.validation.Valid;

public class AdministradorMapper {

    private AdministradorMapper(){}

    public static ReceitaDiaria toEntity(CadastrarReceitaDiariaRequest cadastrarReceitaDiariaRequest) {

        return ReceitaDiaria
                .builder()
                .dataReceita(cadastrarReceitaDiariaRequest.getDataReceita())
                .valor(cadastrarReceitaDiariaRequest.getValor())
                .build();
    }
}
