package br.edu.ifsul.sapucaia.projeto.controller.request.ia;

import java.util.List;

public record BodyRequest(SystemInstructionRequest systemInstruction, List<ContentRequest> contents){ }
