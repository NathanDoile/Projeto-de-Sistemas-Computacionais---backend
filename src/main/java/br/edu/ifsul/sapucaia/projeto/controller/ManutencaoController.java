package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.service.manutencao.CadastrarManutencaoService;
import br.edu.ifsul.sapucaia.projeto.service.manutencao.ListarManutencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/manutencao")
@RequiredArgsConstructor
public class ManutencaoController {

    private final CadastrarManutencaoService cadastrarManutencaoService;

    private final ListarManutencaoService listarManutencaoService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrar(
            @Valid @RequestBody CadastrarManutencaoRequest cadastrarManutencaoRequest) {

        cadastrarManutencaoService.cadastrar(cadastrarManutencaoRequest);
    }

    @GetMapping
    public Page<Manutencao> listar(Pageable pageable) {
        return listarManutencaoService.listar(pageable);
    }
}