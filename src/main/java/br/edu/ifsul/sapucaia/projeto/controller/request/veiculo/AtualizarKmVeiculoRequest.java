package br.edu.ifsul.sapucaia.projeto.controller.request.veiculo;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AtualizarKmVeiculoRequest {

    @NotNull
    private Long idUsuario;

    @NotNull
    private int kmAtualizado;
}
