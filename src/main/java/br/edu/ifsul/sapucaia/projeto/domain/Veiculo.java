package br.edu.ifsul.sapucaia.projeto.domain;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

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

    private String tipo;

    private int ano;

    private String cor;

    private int kmAtual;

    @ManyToOne(mappedBy = "veiculo")
    private Manutencao manutencao;

    @ManyToOne(mappedBy = "veiculo")
    private Custo custo;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
