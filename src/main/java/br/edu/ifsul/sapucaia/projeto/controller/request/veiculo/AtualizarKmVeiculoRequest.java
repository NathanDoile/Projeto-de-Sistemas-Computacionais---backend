package br.edu.ifsul.sapucaia.projeto.controller.request.veiculo;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AtualizarKmVeiculoRequest {

    @NotNull (message = CAMPO_OBRIGATORIO)
    private int kmAtualizado;
    
}
