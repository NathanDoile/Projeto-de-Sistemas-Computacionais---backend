package br.edu.ifsul.sapucaia.projeto.controller.request.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastrarVeiculoRequest {

    @NotBlank(message = "O modelo é obrigatório.")
    private String modelo;

    @NotBlank(message = "A marca é obrigatória.")
    private String marca;

    @NotBlank(message = "A placa é obrigatória.")
    @Size(min = 7, max = 7, message = "A placa deve ter 7 caracteres, sem hífen.")
    private String placa;

    @NotBlank(message = "O tipo é obrigatório.")
    private String tipo;

    @NotNull(message = "O ano é obrigatório.")
    private int ano;

    @NotBlank(message = "A cor é obrigatória.")
    private String cor;

    @NotNull(message = "A quilometragem atual é obrigatória.")
    private int kmAtual;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long idUsuario;
}