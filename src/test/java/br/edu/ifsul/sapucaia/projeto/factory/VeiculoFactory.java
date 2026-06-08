package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.AtualizarKmVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.CadastrarVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoVeiculo.CARRO;
import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.custo;
import static java.time.LocalDate.of;
import static java.time.Month.MAY;

public class VeiculoFactory {
    public static Veiculo veiculo(){
        return Veiculo
                .builder()
                .idVeiculo(1L)
                .modelo("Cronos")
                .dataUltimaAtualizacaoKm(of(2026, MAY, 2))
                .marca("Fiat")
                .placa("RLS3F42")
                .tipo(CARRO)
                .ano(2021)
                .cor("Branco")
                .kmAtual(78300)
                .custos(List.of(custo()))
                .build();
    }

    public static CadastrarVeiculoRequest cadastrarVeiculoRequest() {

        return CadastrarVeiculoRequest
                .builder()
                .modelo("modelo")
                .marca("marca")
                .placa("ABC1D23")
                .tipo("carro")
                .ano(2024)
                .cor("azul")
                .kmAtual(130548)
                .idUsuario(1L)
                .build();
    }

    public static AtualizarKmVeiculoRequest atualizarKmVeiculoRequest() {

        return AtualizarKmVeiculoRequest
                .builder()
                .idUsuario(1L)
                .kmAtualizado(79300)
                .build();
    }
}
