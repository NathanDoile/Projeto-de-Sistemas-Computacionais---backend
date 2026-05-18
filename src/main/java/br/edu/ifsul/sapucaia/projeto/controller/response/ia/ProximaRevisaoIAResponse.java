package br.edu.ifsul.sapucaia.projeto.controller.response.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProximaRevisaoIAResponse(@JsonProperty("revisao_km") int revisaoKm,
                                       @JsonProperty("tempo_entre_manutencao_meses") int tempoEntreManutencaoMeses,
                                       @JsonProperty("distancia_restante_km") int distanciaRestanteKm,
                                       @JsonProperty("itens") List<ItemIAResponse> itens) {
}
