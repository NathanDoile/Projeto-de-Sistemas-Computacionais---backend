package br.edu.ifsul.sapucaia.projeto.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
public class LoginUsuarioResponse {

    private Long idUsuario;

    private String nome;

    private String email;

    private LocalDate dataCadastro;

    private String telefone;

    private boolean notificacaoVencimento;

    private boolean notificacaoManutencao;
}
