package br.edu.ifsul.sapucaia.projeto.domain;

import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
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

    @Enumerated(STRING)
    private FormatoMeta formato;

    private double valorDesejado;

    private double valorAtual;

    private boolean isAtivo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
