package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarSenhaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.LoginUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.service.usuario.AlterarSenhaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.usuario.CadastrarUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.usuario.EditarPerfilUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.usuario.LoginUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final CadastrarUsuarioService cadastrarUsuarioService;
    private final AlterarSenhaUsuarioService alterarSenhaUsuarioService;
    private final EditarPerfilUsuarioService editarPerfilUsuarioService;
    private final LoginUsuarioService loginUsuarioService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void cadastrarUsuario(@Valid @RequestBody CadastrarUsuarioRequest cadastrarUsuarioRequest){
        cadastrarUsuarioService.cadastrarUsuario(cadastrarUsuarioRequest);
    }

    @PutMapping("/{id}/alterar-senha")
    @ResponseStatus(OK)
    public void alterarSenhaUsuario(@PathVariable Long id, @Valid @RequestBody AlterarSenhaUsuarioRequest alterarSenhaUsuarioRequest){
        alterarSenhaUsuarioService.alterarSenhaUsuario(id, alterarSenhaUsuarioRequest);
    }

    @PutMapping("/{id}/editar-perfil")
    @ResponseStatus(OK)
    public void editarPerfilUsuario(@PathVariable Long id, @Valid @RequestBody EditarPerfilUsuarioRequest editarPerfilUsuarioRequest){
        editarPerfilUsuarioService.editarPerfilUsuario(id, editarPerfilUsuarioRequest);
    }
    @PostMapping("/login-usuario")
    @ResponseStatus(OK)
    public void loginUsuario(@Valid @RequestBody LoginUsuarioRequest loginUsuarioRequest){
        loginUsuarioService.loginUsuario(loginUsuarioRequest);
    }
}
