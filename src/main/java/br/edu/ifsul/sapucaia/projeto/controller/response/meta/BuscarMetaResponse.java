package br.edu.ifsul.sapucaia.projeto.controller.response.meta;

import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BuscarMetaResponse {

    private Long idMeta;

    private String titulo;

    private FormatoMeta formato;

    private TipoMeta tipo;

    private double valorDesejado;

    private double valorAtual;

    private double percentualAtingido;
}