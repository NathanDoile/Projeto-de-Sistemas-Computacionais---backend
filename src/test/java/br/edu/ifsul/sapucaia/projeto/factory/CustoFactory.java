package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.custo.EditarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto.COMBUSTIVEL;
import static java.time.LocalDate.now;

public class CustoFactory {

    public static Custo custo() {

        return Custo
                .builder()
                .idCusto(1L)
                .tipo(COMBUSTIVEL)
                .valor(100.00)
                .dataVencimento(null)
                .dataPagamento(now())
                .descricao("Custo de teste")
                .isAtivo(true)
                .manutencao(null)
                .build();
    }

    public static CadastrarCustoRequest cadastrarCustoRequest() {

        return CadastrarCustoRequest
                .builder()
                .idVeiculo(1L)
                .tipo("COMBUSTIVEL")
                .valor(100.00)
                .descricao("Custo de teste")
                .dataVencimento(now().plusDays(5))
                .dataPagamento(now())
                .build();
    }

    public static EditarCustoRequest editarCustoRequest() {

        return EditarCustoRequest
                .builder()
                .idCusto(1L)
                .tipo(COMBUSTIVEL.getDescricao())
                .valor(220.0)
                .dataVencimento(now().plusDays(10))
                .dataPagamento(now())
                .descricao("Teste de edição de custo")
                .build();
    }

}