package br.edu.ifsul.sapucaia.projeto.controller.response.usuario;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CadastrarUsuarioResponse {

    private Long idUsuario;

    private String nome;

    private String email;

    private LocalDate dataCadastro;

    private String telefone;
}
