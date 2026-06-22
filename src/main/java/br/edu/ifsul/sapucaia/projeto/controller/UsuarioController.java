package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.*;
import br.edu.ifsul.sapucaia.projeto.controller.response.usuario.CadastrarUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.service.usuario.*;
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
    private final ExcluirContaUsuarioService excluirContaUsuarioService;
    private final AlterarNotificacoesService alterarNotificacoesService;

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

    @PutMapping("/editar-perfil")
    @ResponseStatus(OK)
    public void editarPerfilUsuario(
            @Valid @RequestBody EditarPerfilUsuarioRequest request
    ) {
        editarPerfilUsuarioService.editarPerfilUsuario(request);
    }

    @DeleteMapping("/excluir-conta")
    @ResponseStatus(OK)
    public void excluirConta(
            @Valid @RequestBody ExcluirContaUsuarioRequest request
    ) {
        excluirContaUsuarioService.excluirConta(request);
    }

    @PutMapping("/notificacoes/{id}")
    @ResponseStatus(OK)
    public void alterarNotificacoes(@PathVariable Long id, @Valid @RequestBody AlterarNotificacoesRequest request){
        alterarNotificacoesService.alterarNotificacoes(id, request);
    }
}
