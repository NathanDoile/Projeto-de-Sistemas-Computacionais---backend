package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustoRepository extends JpaRepository<Custo, Long> {

    boolean existsByIdCustoAndIsAtivo(@NotNull Long idCusto, boolean isAtivo);

    Optional<Custo> findByIdCustoAndIsAtivo(Long idCusto, boolean isAtivo);

    List<Custo> findByVeiculoIdVeiculo(Long idVeiculo);

    List<Custo> findByVeiculoIdVeiculoAndIsAtivo(Long idVeiculo, boolean isAtivo);

    List<Custo> findByVeiculoIdVeiculoAndDataPagamentoBetween(Long idVeiculo, LocalDate inicioSemana, LocalDate hoje);

    Collection<Custo> findByVeiculoIdVeiculoBetween(
            Long idUsuario,
            LocalDate inicioMes,
            LocalDate fimMes
    );

    Page<Custo> findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(Veiculo veiculo, boolean isAtivo, Pageable pageable);
}
