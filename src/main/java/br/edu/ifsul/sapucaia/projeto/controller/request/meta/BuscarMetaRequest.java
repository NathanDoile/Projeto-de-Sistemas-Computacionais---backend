package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BuscarMetaRequest {

    @NotNull
    private Long idUsuario;
}
