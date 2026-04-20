package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.EditarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.service.custo.BuscarCustosEmAbertoService;
import br.edu.ifsul.sapucaia.projeto.service.custo.EditarCustoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.service.custo.CadastrarCustoService;
import jakarta.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/custo")
@RequiredArgsConstructor
public class CustoController {

    private final CadastrarCustoService cadastrarCustoService;

    private final EditarCustoService editarCustoService;

    private final BuscarCustosEmAbertoService buscarCustosEmAbertoService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(@Valid @RequestBody CadastrarCustoRequest cadastrarCustoRequest){
        cadastrarCustoService.cadastrar(cadastrarCustoRequest);
    }

    @PutMapping("/editar")
    @ResponseStatus(OK)
    public void editar(@Valid @RequestBody EditarCustoRequest editarCustoRequest){
        editarCustoService.editar(editarCustoRequest);
    }

    @GetMapping("/{id}/em-aberto")
    @ResponseStatus(OK)
    public List<BuscarCustosEmAbertoResponse> buscarCustosEmAberto(@PathVariable Long id){
        return buscarCustosEmAbertoService.buscar(id);
    }
}
