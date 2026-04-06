package br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CadastrarReceitaDiariaRequest {

    @NotNull
    private LocalDate dataReceita;

    @NotNull
    private Double valor;

    @NotNull
    private Long idUsuario;
}
