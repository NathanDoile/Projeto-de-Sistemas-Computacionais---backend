package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;

import java.time.LocalDate;

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

    public static EditarPerfilUsuarioRequest editarPerfilUsuarioRequest() {

        return EditarPerfilUsuarioRequest
                .builder()
                .nome("Novo nome Teste")
                .email("novoemailteste@gmail.com")
                .telefone("51999999999")
                .build();
    }
}
