package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.service.meta.CadastrarMetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/meta")
@RequiredArgsConstructor
public class MetaController {

    private final CadastrarMetaService cadastrarMetaService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarMetaRequest cadastrarMetaRequest) {
        cadastrarMetaService.cadastrar(cadastrarMetaRequest);
    }
    
}
