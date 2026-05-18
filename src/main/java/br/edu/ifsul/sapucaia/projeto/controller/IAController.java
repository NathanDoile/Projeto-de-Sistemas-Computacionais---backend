package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.service.ia.IAService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IAController {

    final IAService iaService;

    public IAController(IAService iaService) {
        this.iaService = iaService;
    }

    @GetMapping("/ia")
    public ManutencaoIAResponse chatIA(@RequestParam (value = "message") String mensagem){
        return iaService.chamadaChat(mensagem);
    }
}
