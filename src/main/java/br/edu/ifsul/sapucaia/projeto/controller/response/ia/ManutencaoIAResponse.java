package br.edu.ifsul.sapucaia.projeto.controller.response.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ManutencaoIAResponse(VeiculoIAResponse veiculo, @JsonProperty("proximas_revisoes") List<ProximaRevisaoIAResponse> proximaRevisao) {
}
