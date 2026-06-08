package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.*;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.*;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.RECEITA;
import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.meta;
import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.receitaDiaria;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static java.time.LocalDate.of;
import static java.time.Month.APRIL;

public class UsuarioFactory {
    public static Usuario usuario() {

        return Usuario
                .builder()
                .idUsuario(1L)
                .nome("Nome Teste")
                .email("emailteste@gmail.com")
                .senha("senhadeteste")
                .dataCadastro(of(2026, APRIL, 24))
                .telefone("51888888888")
                .isAtivo(true)
                .possuiVeiculo(true)
                .notificacaoManutencao(true)
                .notificacaoVencimento(true)
                .metas(List.of(meta(DIARIA, RECEITA), meta(SEMANAL, RECEITA), meta(MENSAL, RECEITA)))
                .veiculo(veiculo())
                .receitasDiarias(List.of(receitaDiaria()))
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

    public static AlterarNotificacoesRequest alterarNotificacaoManutencaoRequest() {

        return AlterarNotificacoesRequest
                .builder()
                .notificacao("Manutenção")
                .build();
    }

    public static AlterarNotificacoesRequest alterarNotificacaoVencimentoRequest() {

        return AlterarNotificacoesRequest
                .builder()
                .notificacao("Vencimento")
                .build();
    }

}
