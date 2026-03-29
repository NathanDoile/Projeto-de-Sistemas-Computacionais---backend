package br.edu.ifsul.sapucaia.projeto.domain;

import java.time.LocalDate;

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

    private String tipo;

    private LocalDate dataManutencao;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_veiculo")
    private Veiculo veiculo;

    @OneToOne
    @JoinColumn(name = "id_custo")
    private Custo custo;
    
}
