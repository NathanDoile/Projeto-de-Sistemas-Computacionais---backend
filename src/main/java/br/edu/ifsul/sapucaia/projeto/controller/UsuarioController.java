package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarSenhaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.ExcluirContaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.LoginUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.usuario.CadastrarUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.service.usuario.AlterarSenhaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.usuario.CadastrarUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.usuario.EditarPerfilUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.usuario.ExcluirContaUsuarioService;
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
    private final ExcluirContaUsuarioService excluirContaUsuarioService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CadastrarUsuarioResponse cadastrarUsuario(@Valid @RequestBody CadastrarUsuarioRequest cadastrarUsuarioRequest){
        return cadastrarUsuarioService.cadastrarUsuario(cadastrarUsuarioRequest);
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

    @DeleteMapping("/{id}/excluir-conta")
    @ResponseStatus(OK)
    public void excluirConta(@PathVariable Long id, @Valid @RequestBody ExcluirContaUsuarioRequest excluirContaUsuarioRequest){
        excluirContaUsuarioService.excluirConta(id, excluirContaUsuarioRequest);
    }

}
