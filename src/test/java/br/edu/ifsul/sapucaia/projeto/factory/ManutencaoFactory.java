package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static java.time.LocalDate.now;

public class ManutencaoFactory {

    public static Manutencao manutencao() {

        return Manutencao
                .builder()
                .idManutencao(1L)
                .tipo(TipoManutencao.PREVENTIVA)
                .dataManutencao(now().minusDays(1))
                .descricao("Manutenção de teste")
                .custo(custo())
                .isAtivo(true)
                .build();
    }

    public static CadastrarManutencaoRequest cadastrarManutencaoRequest() {

        return CadastrarManutencaoRequest
                .builder()
                .tipo(TipoManutencao.PREVENTIVA.getDescricao())
                .dataManutencao(now().minusDays(1))
                .descricao("Revisão de teste")
                .idVeiculo(1L)
                .valor(200)
                .build();
    }

}
