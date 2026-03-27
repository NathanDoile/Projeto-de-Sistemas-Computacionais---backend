package br.edu.ifsul.sapucaia.projeto.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Long id;

    private String nome;

    private String email;

    private String senha;

    private LocalDate dataCadastro;

    private Long telefone;

    @OneToMany(mappedBy = "usuario")
    private List<Meta> metas;

    @OneToMany(mappedBy = "usuario")
    private List<Veiculo> veiculos;

    @OneToMany(mappedBy = "usuario")
    private List<ReceitaDiaria> receitasDiarias;
}
