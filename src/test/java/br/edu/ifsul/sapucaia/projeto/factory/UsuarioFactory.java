package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.*;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;

import static java.time.LocalDate.of;

public class UsuarioFactory {
    public static Usuario usuario() {

        return Usuario
                .builder()
                .idUsuario(1L)
                .nome("Nome Teste")
                .email("emailteste@gmail.com")
                .senha("senhadeteste")
                .dataCadastro(of(2026, 04, 24))
                .telefone("51888888888")
                .isAtivo(true)
                .possuiVeiculo(true)
                .notificacaoManutencao(true)
                .notificacaoVencimento(true)
                .build();
    }

    public static CadastrarUsuarioRequest cadastrarUsuarioRequest(){
        return CadastrarUsuarioRequest
                .builder()
                .nome("Nome cadastro Teste")
                .email("emailcadastroteste@gmail.com")
                .senha("senhadeteste")
                .telefone("51888888888")
                .build();
    }

    public static EditarPerfilUsuarioRequest editarPerfilUsuarioRequest() {

        return EditarPerfilUsuarioRequest
                .builder()
                .nome("Novo nome Teste")
                .email("novoemailteste@gmail.com")
                .telefone("51999999999")
                .build();
    }

    public static ExcluirContaUsuarioRequest excluirContaUsuarioRequest() {

        return ExcluirContaUsuarioRequest
                .builder()
                .senha("senhadeteste")
                .build();
    }

    public static AlterarSenhaUsuarioRequest alterarSenhaUsuarioRequest() {

        return AlterarSenhaUsuarioRequest
                .builder()
                .senhaAtual("senhadeteste")
                .novaSenha("novasenhadeteste")
                .build();
    }

    public static LoginUsuarioRequest LoginUsuarioRequest() {

        return LoginUsuarioRequest
                .builder()
                .email("emailteste@gmail.com")
                .senha("senhadeteste")
                .build();
    }

    public static AlterarNotificacaoManutencaoRequest alterarNotificacaoManutencaoRequest() {

        return AlterarNotificacaoManutencaoRequest
                .builder()
                .notificacaoManutencao(false)
                .build();
    }

    public static AlterarNotificacaoVencimentoRequest alterarNotificacaoVencimentoRequest() {

        return AlterarNotificacaoVencimentoRequest
                .builder()
                .notificacaoVencimento(false)
                .build();
    }

}
