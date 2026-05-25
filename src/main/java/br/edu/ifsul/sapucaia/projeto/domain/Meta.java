package br.edu.ifsul.sapucaia.projeto.domain;

import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta;
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

    @Enumerated(STRING)
    private TipoMeta tipo;

    private double valorDesejado;

    private double valorAtual;

    private boolean isAtivo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
