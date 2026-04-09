package br.edu.ifsul.sapucaia.projeto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.service.custo.CadastrarCustoService;
import jakarta.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/custo")
@RequiredArgsConstructor
public class CustoController {

    private final CadastrarCustoService cadastrarCustoService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarCustoRequest cadastrarCustoRequest){
        cadastrarCustoService.cadastrar(    cadastrarCustoRequest);
    }

}
