package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.service.receita_diaria.CadastrarReceitaDiariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/receita-diaria")
@RequiredArgsConstructor
public class ReceitaDiariaController {

    private final CadastrarReceitaDiariaService cadastrarReceitaDiariaService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarReceitaDiariaRequest cadastrarReceitaDiariaRequest){
        cadastrarReceitaDiariaService.cadastrar(cadastrarReceitaDiariaRequest);
    }

}
