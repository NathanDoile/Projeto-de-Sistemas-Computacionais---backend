package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;

import static java.time.LocalDate.of;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoVeiculo.CARRO;

public class VeiculoFactory {
    public static Veiculo veiculo(){
        return Veiculo
                .builder()
                .idVeiculo(1L)
                .modelo("Cronos")
                .dataUltimaAtualizacaoKm(of(2026, 05, 02))
                .marca("Fiat")
                .placa("RLS3F42")
                .tipo(CARRO)
                .ano(2021)
                .cor("Branco")
                .kmAtual(78300)
                .build();
    }
}
