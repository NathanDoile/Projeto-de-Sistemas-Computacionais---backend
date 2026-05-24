package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.meta.BuscarMetaResponse;
import br.edu.ifsul.sapucaia.projeto.service.meta.BuscarMetasService;
import br.edu.ifsul.sapucaia.projeto.service.meta.CadastrarMetaService;
import br.edu.ifsul.sapucaia.projeto.service.meta.DeletarMetaService;
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

    private final DeletarMetaService deletarMetaService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarMetaRequest cadastrarMetaRequest) {
        cadastrarMetaService.cadastrar(cadastrarMetaRequest);
    }

    @GetMapping("/{idUsuario}")
    @ResponseStatus(OK)
    public List<BuscarMetaResponse> buscarMetas(@PathVariable Long idUsuario){
        return buscarMetasService.buscar(idUsuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deletar(@PathVariable Long id){
        deletarMetaService.deletar(id);
    }
}
