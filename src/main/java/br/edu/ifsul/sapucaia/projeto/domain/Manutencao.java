package br.edu.ifsul.sapucaia.projeto.domain;

import static jakarta.persistence.EnumType.STRING;

import java.time.LocalDate;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoManutencao;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idManutencao;

    @Enumerated(STRING)
    private TipoManutencao tipo;

    private LocalDate dataManutencao;

    private String descricao;

    private boolean isAtivo;

    @ManyToOne
    @JoinColumn(name = "id_veiculo")
    private Veiculo veiculo;

    @OneToOne
    @JoinColumn(name = "id_custo")
    private Custo custo;
    
}
