package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.service.relatorios.InformacoesDaSemanaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatoriosController {

    private final InformacoesDaSemanaService informacoesDaSemanaService;

    @GetMapping("/informacoes-semana/{idUsuario}")
    public InformacoesDaSemanaResponse  getInformacoesSemana(@PathVariable Long idUsuario){
        InformacoesDaSemanaResponse informacoes = informacoesDaSemanaService.buscarInformacoesDaSemana(idUsuario);

        return informacoes;
    }
}
