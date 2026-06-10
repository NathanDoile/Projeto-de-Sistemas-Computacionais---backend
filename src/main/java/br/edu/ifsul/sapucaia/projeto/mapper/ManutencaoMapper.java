package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.manutencao.ManutencaoResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.ExportarManutencoesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;

import java.time.format.DateTimeFormatter;

public class ManutencaoMapper {

    private ManutencaoMapper(){}


    public static Manutencao toEntity(CadastrarManutencaoRequest cadastrarManutencaoRequest) {

        return Manutencao
                .builder()
                .tipo(TipoManutencao.deTexto(cadastrarManutencaoRequest.getTipo()))
                .dataManutencao(cadastrarManutencaoRequest.getDataManutencao())
                .descricao(cadastrarManutencaoRequest.getDescricao())
                .build();
    }

    public static ExportarManutencoesResponse toResponse(Manutencao manutencao){

        return ExportarManutencoesResponse
                .builder()
                .tipo(manutencao.getTipo().getDescricao())
                .dataManutencao(manutencao.getDataManutencao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .descricao(manutencao.getDescricao())
                .valor(manutencao.getCusto().getValor())
                .build();
    }

    public static ManutencaoResponse toManutencaoResponse(Manutencao manutencao) {

        return ManutencaoResponse.builder()
                .idManutencao(manutencao.getIdManutencao())
                .tipo(manutencao.getTipo())
                .dataManutencao(manutencao.getDataManutencao())
                .descricao(manutencao.getDescricao())
                .build();
    }
}
