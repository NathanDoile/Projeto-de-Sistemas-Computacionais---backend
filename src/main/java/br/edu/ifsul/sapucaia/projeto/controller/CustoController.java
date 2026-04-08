/*package br.edu.ifsul.sapucaia.projeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.service.meta.CadastrarCustoService;
import jakarta.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/custo")
public class CustoController {

    @Autowired
    private CadastrarCustoService cadastrarCustoService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarCustoRequest cadastrarCustoRequest) {
        cadastrarCustoService.cadastrar(cadastrarCustoRequest);
    }
    
}
*/