package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class BuscarMetaRequest {

    @NotNull (message = "O ID do usuário é obrigatório.")
    private Long idUsuario ;
}
