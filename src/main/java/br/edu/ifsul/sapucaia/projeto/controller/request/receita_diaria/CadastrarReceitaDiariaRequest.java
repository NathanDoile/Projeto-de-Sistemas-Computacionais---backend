package br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CadastrarReceitaDiariaRequest {

    @NotNull (message = "A data é obrigatória.")
    private LocalDate dataReceita;

    @NotNull (message = "O valor é obrigatório.")
    private Double valor;

    @NotNull (message = "O ID do usuário é orbigatório.")
    private Long idUsuario;
}
