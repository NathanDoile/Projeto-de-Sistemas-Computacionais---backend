package br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CadastrarReceitaDiariaRequest {

    @NotNull (message = "A data da receita é obrigatória.")
    private LocalDate dataReceita;

    @NotNull (message = "O valor da receita é obrigatória.")
    private Double valor;

    @NotNull (message = "O ID do usuário é orbigatório.")
    private Long idUsuario;
}
