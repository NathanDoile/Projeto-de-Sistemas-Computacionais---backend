package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.CadastrarVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.CadastrarVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;

import java.time.LocalDate;

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

    public static CadastrarVeiculoResponse toResponse(Veiculo veiculo) {

        return CadastrarVeiculoResponse
                .builder()
                .idVeiculo(veiculo.getIdVeiculo())
                .modelo(veiculo.getModelo())
                .dataUltimaAtualizacaoKm(veiculo.getDataUltimaAtualizacaoKm())
                .marca(veiculo.getMarca())
                .placa(veiculo.getPlaca())
                .tipo(veiculo.getTipo())
                .ano(veiculo.getAno())
                .cor(veiculo.getCor())
                .kmAtual(veiculo.getKmAtual())
                .build();
    }
}
