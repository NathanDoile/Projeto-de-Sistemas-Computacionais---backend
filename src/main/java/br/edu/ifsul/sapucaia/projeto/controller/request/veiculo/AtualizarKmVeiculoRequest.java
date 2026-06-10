package br.edu.ifsul.sapucaia.projeto.controller.request.veiculo;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AtualizarKmVeiculoRequest {

    @NotNull (message = "O ID do usuário é obrigatório.")
    private Long idUsuario;

    @NotNull (message = "O novo valor do KM é obrigatório.")
    private int kmAtualizado;
}
