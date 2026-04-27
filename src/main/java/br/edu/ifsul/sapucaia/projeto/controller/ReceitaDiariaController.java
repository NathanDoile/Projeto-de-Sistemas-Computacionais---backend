package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GanhoLiquidoMesResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.service.receita_diaria.CadastrarReceitaDiariaService;
import br.edu.ifsul.sapucaia.projeto.service.relatorios.GanhoLiquidoMesService;
import br.edu.ifsul.sapucaia.projeto.service.relatorios.ReceitaSemanalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/receita-diaria")
@RequiredArgsConstructor
public class ReceitaDiariaController {

    private final CadastrarReceitaDiariaService cadastrarReceitaDiariaService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarReceitaDiariaRequest request){
        cadastrarReceitaDiariaService.cadastrar(request);
    }
}