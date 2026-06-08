package br.edu.ifsul.sapucaia.projeto.controller.response.manutencao;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ManutencaoResponse(
        Long idManutencao,
        TipoManutencao tipo,
        LocalDate dataManutencao,
        String descricao
){
}