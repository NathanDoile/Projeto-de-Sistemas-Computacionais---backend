package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;

public class CustoMapper {

        private CustoMapper(){}
        public static Custo toEntity(CadastrarCustoRequest cadastrarCustoRequest) {

        return Custo
                .builder()
                .tipo(cadastrarCustoRequest.getTipo())
                .valor(cadastrarCustoRequest.getValor())
                .dataVencimento(cadastrarCustoRequest.getDataVencimento())
                .dataPagamento(cadastrarCustoRequest.getDataPagamento())
                .descricao(cadastrarCustoRequest.getDescricao())
                .build();
        }
}
