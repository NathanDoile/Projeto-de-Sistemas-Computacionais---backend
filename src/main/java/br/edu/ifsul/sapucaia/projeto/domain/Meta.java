package br.edu.ifsul.sapucaia.projeto.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Meta {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idMeta;

    private String titulo;

    private String formato;

    private double valorDesejado;

    private double valorAtual;

    private boolean isAtivo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
