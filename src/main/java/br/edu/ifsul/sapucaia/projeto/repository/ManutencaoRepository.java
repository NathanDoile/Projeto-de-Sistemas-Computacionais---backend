package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    Page<Manutencao> findAllByIsAtivo(boolean isAtivo, Pageable pageable);

    List<Manutencao> findAllByVeiculoIdVeiculoAndIsAtivoAndDataManutencaoBetween(Long idVeiculo, boolean b, LocalDate localDate, LocalDate localDate1);

    Page<Manutencao> findAllByVeiculoUsuarioIdUsuarioAndIsAtivo(
            Long idUsuario,
            boolean isAtivo,
            Pageable pageable
    );

    List<Manutencao> findAllByVeiculoIdVeiculoAndIsAtivo(Long idVeiculo, boolean b);

    List<Manutencao> findAllByVeiculoIdVeiculo(Long idVeiculo);
    
}
