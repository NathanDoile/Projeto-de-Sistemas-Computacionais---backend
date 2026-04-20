package br.edu.ifsul.sapucaia.projeto.controller.response.custo;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class BuscarCustosEmAbertoResponse {

    private Long idCusto;

    private String descricao;

    private LocalDate dataVencimento;

    private double valor;

    private String tipo;
}
