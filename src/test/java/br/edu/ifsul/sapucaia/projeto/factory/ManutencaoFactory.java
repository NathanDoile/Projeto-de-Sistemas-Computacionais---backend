package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;

public class ManutencaoFactory {

    public static Manutencao manutencao() {

        return Manutencao
                .builder()
                .idManutencao(1L)
                .tipo(TipoManutencao.PREVENTIVA)
                .dataManutencao(DateNow.now().minusDays(1))
                .descricao("Manutenção de teste")
                .isAtivo(true)
                .build();
    }

    public static CadastrarManutencaoRequest cadastrarManutencaoRequest() {

        return CadastrarManutencaoRequest
                .builder()
                .tipo(TipoManutencao.PREVENTIVA.getDescricao())
                .dataManutencao(DateNow.now().minusDays(1))
                .descricao("Revisão de teste")
                .valor(200)
                .build();
    }

}
