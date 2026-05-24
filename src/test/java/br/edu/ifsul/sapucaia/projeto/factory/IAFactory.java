package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.response.ia.*;

import java.util.List;

public class IAFactory {

    public static ManutencaoIAResponse response(){
        ManutencaoIAResponse resposta = new ManutencaoIAResponse(
                new VeiculoIAResponse("Hyundai", "HB 20", "2023", 100000),
                new ProximaRevisaoIAResponse(5000, 6, 5000)
        );
        return resposta;
    }

    public static IaResponse responseIA(){
        IaResponse resposta = new IaResponse(List.of(new CandidateResponse(
                new ContentResponse(List.of(new PartResponse("marca: Fiat")))
        )));
        return resposta;
    }

}
