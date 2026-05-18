package br.edu.ifsul.sapucaia.projeto.controller.response.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VeiculoIAResponse(String marca, String modelo, String ano, @JsonProperty("quilometragem_atual") int kmAtual) {}
