package br.edu.ifsul.sapucaia.projeto.controller.request.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastrarVeiculoRequest {

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String modelo;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String marca;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    @Size(min = CAMPO_TAMANHO_MAXIMO_MINIMO_PLACA, max = CAMPO_TAMANHO_MAXIMO_MINIMO_PLACA, message = CAMPO_PLACA)
    private String placa;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String tipo;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private int ano;

    @NotBlank(message = CAMPO_OBRIGATORIO)
    private String cor;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private int kmAtual;

    @NotNull(message = CAMPO_OBRIGATORIO)
    private Long idUsuario;
}