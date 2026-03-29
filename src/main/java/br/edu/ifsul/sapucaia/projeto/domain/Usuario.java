package br.edu.ifsul.sapucaia.projeto.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idUsuario;

    private String nome;

    private String email;

    private String senha;

    private LocalDate dataCadastro;

    private String telefone;

    @OneToMany(mappedBy = "usuario")
    private List<Meta> metas;

    @OneToOne(mappedBy = "usuario")
    private Veiculo veiculo;

    @OneToMany(mappedBy = "usuario")
    private List<ReceitaDiaria> receitasDiarias;
}
