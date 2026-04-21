package br.edu.ifsul.sapucaia.projeto.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
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

    @CreationTimestamp
    private LocalDate dataCadastro;

    private String telefone;

    private boolean isAtivo;

    private boolean possuiVeiculo;

    private boolean notificacaoVencimento;

    private boolean notificacaoManutencao;

    @OneToMany(mappedBy = "usuario", cascade = REMOVE, orphanRemoval = true)
    private List<Meta> metas;

    @OneToOne(mappedBy = "usuario", cascade = REMOVE, orphanRemoval = true)
    private Veiculo veiculo;

    @OneToMany(mappedBy = "usuario", cascade = REMOVE, orphanRemoval = true)
    private List<ReceitaDiaria> receitasDiarias;
}
