package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PendenciasDoUsuarioResponse {

    private boolean kmDesatualizado;

    private boolean manutencaoKmVencido;
    
    private boolean manutencaoTempoVencido;
    
}