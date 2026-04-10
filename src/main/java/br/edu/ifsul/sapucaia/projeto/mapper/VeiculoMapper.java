package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.CadastrarVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;

public class VeiculoMapper {

    private VeiculoMapper(){}


    public static Veiculo toEntity(CadastrarVeiculoRequest cadastrarVeiculoRequest) {

        return Veiculo
                .builder()
                .modelo(cadastrarVeiculoRequest.getModelo())
                .marca(cadastrarVeiculoRequest.getMarca())
                .placa(cadastrarVeiculoRequest.getPlaca())
                .tipo(cadastrarVeiculoRequest.getTipo())
                .ano(cadastrarVeiculoRequest.getAno())
                .cor(cadastrarVeiculoRequest.getCor())
                .kmAtual(cadastrarVeiculoRequest.getKmAtual())
                .build();
    }
}
