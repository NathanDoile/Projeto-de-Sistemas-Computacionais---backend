package br.edu.ifsul.sapucaia.projeto.controller.response.veiculo;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CadastrarVeiculoResponse {

    private Long idVeiculo;

    private String modelo;

    private LocalDate dataUltimaAtualizacaoKm;

    private String marca;

    private String placa;

    private String tipo;

    private int ano;

    private String cor;

    private int kmAtual;
}
