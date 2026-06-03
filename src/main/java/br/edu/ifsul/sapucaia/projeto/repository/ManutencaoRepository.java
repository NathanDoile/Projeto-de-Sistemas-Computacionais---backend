package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    List<Manutencao> findAllByVeiculoIdVeiculoAndIsAtivo(Long idVeiculo, boolean isAtivo);

    Page<Manutencao> findAllByIsAtivo(boolean isAtivo, Pageable pageable);
}
