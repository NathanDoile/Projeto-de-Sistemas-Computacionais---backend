package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class BuscarMetaRequest {

    @NotNull (message = CAMPO_OBRIGATORIO)
    private Long idUsuario ;
}
