package br.edu.ifsul.sapucaia.projeto.controller.request.manutencao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
public class CadastrarManutencaoRequest {

    @NotBlank
    private String tipo;

    @NotNull
    private LocalDate dataManutencao;

    @NotBlank
    private String descricao;

    @NotNull
    private Long idVeiculo;

    @NotNull
    private Long idCusto;
}
