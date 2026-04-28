package br.edu.ifsul.sapucaia.projeto.controller.response.relatorios;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class UltimasTransacoesResponse {

    private String descricao;

    private TipoCusto categoria;

    private double valor;

    private LocalDate data;

    private String tipoTransacao;

}