package br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

import static br.edu.ifsul.sapucaia.projeto.controller.request.ErroRequest.CAMPO_OBRIGATORIO;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CadastrarReceitaDiariaRequest {

    @NotNull (message = CAMPO_OBRIGATORIO)
    private LocalDate dataReceita;

    @NotNull (message = CAMPO_OBRIGATORIO)
    private Double valor;
}
