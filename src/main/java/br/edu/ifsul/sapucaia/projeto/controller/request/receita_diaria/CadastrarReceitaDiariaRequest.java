package br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CadastrarReceitaDiariaRequest {

    @NotNull
    private LocalDate dataReceita;

    @NotNull
    private Double valor;

    @NotNull
    private Long idUsuario;
}
