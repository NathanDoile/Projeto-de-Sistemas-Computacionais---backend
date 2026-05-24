package br.edu.ifsul.sapucaia.projeto.controller.response.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ManutencaoIAResponse(VeiculoIAResponse veiculo, @JsonProperty("proxima_revisao") ProximaRevisaoIAResponse proximaRevisao) {
}
