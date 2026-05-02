package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.CadastrarVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.CadastrarVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.RetornaDadosVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.service.veiculo.CadastrarVeiculoService;
import br.edu.ifsul.sapucaia.projeto.service.veiculo.RetornaDadosVeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/veiculo")
@RequiredArgsConstructor
public class VeiculoController {

    private final CadastrarVeiculoService cadastrarVeiculoService;

    private final RetornaDadosVeiculoService retornaDadosVeiculoService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CadastrarVeiculoResponse cadastrar(@Valid @RequestBody CadastrarVeiculoRequest cadastrarVeiculoRequest){
         return cadastrarVeiculoService.cadastrar(cadastrarVeiculoRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public RetornaDadosVeiculoResponse retornaDadosVeiculo(@PathVariable Long id){
        return retornaDadosVeiculoService.dadosVeiculo(id);
    }
}
