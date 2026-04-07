package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.service.usuario.CadastrarUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private CadastrarUsuarioService cadastrarUsuarioService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrarUsuario(@Valid @RequestBody CadastrarUsuarioRequest cadastrarUsuarioRequest){
        cadastrarUsuarioService.cadastrarUsuario(cadastrarUsuarioRequest);
    }

}
