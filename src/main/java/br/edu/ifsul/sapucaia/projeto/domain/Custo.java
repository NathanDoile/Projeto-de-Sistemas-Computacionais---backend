package br.edu.ifsul.sapucaia.projeto.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.EnumType.STRING;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Custo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idCusto;

    @Enumerated(STRING)
    private TipoCusto tipo;

    private double valor;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    private String descricao;

    private boolean isAtivo;

    @ManyToOne
    @JoinColumn(name = "id_veiculo")
    private Veiculo veiculo;

    @OneToOne(mappedBy = "custo")
    private Manutencao manutencao;

}