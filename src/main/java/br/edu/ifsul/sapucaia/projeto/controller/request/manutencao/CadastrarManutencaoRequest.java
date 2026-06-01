package br.edu.ifsul.sapucaia.projeto.controller.request.manutencao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CadastrarManutencaoRequest {

    @NotBlank(message = "O tipo da manutenção é obrigatório.")
    private String tipo;

    @NotNull(message = "A data da manutenção é obrigatória.")
    private LocalDate dataManutencao;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "O ID do veículo é obrigatório.")
    private Long idVeiculo;

    @NotNull(message = "O ID do custo é obrigatório.")
    private Long idCusto;
}