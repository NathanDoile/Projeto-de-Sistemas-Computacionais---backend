package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CadastrarMetaRequest {

    @NotNull
    private String titulo;

    @NotNull
    private String formato;

    @NotNull
    private Double valorMeta;

    @NotNull
    private Long idUsuario;
    
}
