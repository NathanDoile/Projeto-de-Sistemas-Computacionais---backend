package br.edu.ifsul.sapucaia.projeto.controller.request.meta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CadastrarMetaRequest {

    @NotBlank
    private String titulo;

    @NotBlank
    private String formato;

    @NotNull
    private Double valor;

    @NotNull
    private Long idUsuario;
    
}
