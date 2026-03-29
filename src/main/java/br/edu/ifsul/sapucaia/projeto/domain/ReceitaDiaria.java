package br.edu.ifsul.sapucaia.projeto.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ReceitaDiaria {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idReceita;

    private LocalDate dataReceita;

    private Double valor;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
