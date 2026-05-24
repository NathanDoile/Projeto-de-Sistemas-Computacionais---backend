package br.edu.ifsul.sapucaia.projeto.controller.response.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProximaRevisaoIAResponse(@JsonProperty("intervalo_manutencoes_km") int intervaloManutencoesKm,
                                       @JsonProperty("intervalo_manutencoes_meses") int intervaloManutencoesMeses,
                                       @JsonProperty("distancia_restante_km") int distanciaRestanteKm) {
}
