package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.BuscarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.meta.BuscarMetaResponse;
import br.edu.ifsul.sapucaia.projeto.service.meta.BuscarMetasService;
import br.edu.ifsul.sapucaia.projeto.service.meta.CadastrarMetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/meta")
@RequiredArgsConstructor
public class MetaController {

    private final CadastrarMetaService cadastrarMetaService;

    private final BuscarMetasService buscarMetasService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarMetaRequest cadastrarMetaRequest) {
        cadastrarMetaService.cadastrar(cadastrarMetaRequest);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<BuscarMetaResponse> buscarMetas(@Valid @RequestBody BuscarMetaRequest request){
        return buscarMetasService.buscar(request);
    }
    
}
