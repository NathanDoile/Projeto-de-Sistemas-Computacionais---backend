package br.edu.ifsul.sapucaia.projeto.domain;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVeiculo;

    private String modelo;

    private LocalDate dataUltimaAtualizacaoKm;

    private String marca;

    private String placa;

    @Enumerated(STRING)
    private TipoVeiculo tipo;

    private int ano;

    private String cor;

    private int kmAtual;

    private boolean isAtivo;

    @OneToMany(mappedBy = "veiculo")
    private List<Manutencao> manutencoes;

    @OneToMany(mappedBy = "veiculo")
    private List<Custo> custos;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}