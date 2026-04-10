package br.edu.ifsul.sapucaia.projeto.controller.request.veiculo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CadastrarVeiculoRequest {

    @NotBlank
    private String modelo;

    @NotBlank
    private String marca;

    @NotBlank
    @Size(min = 7, max = 7, message = "A placa deve ter 7 caracteres, sem hífen.")
    private String placa;

    @NotBlank
    private String tipo;

    @NotNull
    private int ano;

    @NotBlank
    private String cor;

    @NotNull
    private int kmAtual;

    @NotNull
    private Long idUsuario;
}
