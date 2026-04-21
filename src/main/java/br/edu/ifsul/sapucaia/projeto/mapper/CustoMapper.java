package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;

public class CustoMapper {

        private CustoMapper(){}
        public static Custo toEntity(CadastrarCustoRequest cadastrarCustoRequest) {

        return Custo
                .builder()
                .tipo(TipoCusto.deTexto(cadastrarCustoRequest.getTipo()))
                .valor(cadastrarCustoRequest.getValor())
                .dataVencimento(cadastrarCustoRequest.getDataVencimento())
                .dataPagamento(cadastrarCustoRequest.getDataPagamento())
                .descricao(cadastrarCustoRequest.getDescricao())
                .build();
        }

    public static BuscarCustosEmAbertoResponse toResponse(Custo custo) {

            return BuscarCustosEmAbertoResponse
                    .builder()
                    .idCusto(custo.getIdCusto())
                    .descricao(custo.getDescricao())
                    .dataVencimento(custo.getDataVencimento())
                    .valor(custo.getValor())
                    .tipo(custo.getTipo().getDescricao())
                    .build();
    }
}
