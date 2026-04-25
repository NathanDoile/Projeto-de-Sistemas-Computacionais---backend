package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;

import java.time.LocalDate;
import java.util.List;

public interface CustoRepository extends JpaRepository<Custo, Long> {

    boolean existsByIdCustoAndIsAtivo(@NotNull Long idCusto, boolean isAtivo);

    List<Custo> findByVeiculoAndDataPagamentoIsNullAndIsAtivo(Veiculo veiculo, boolean b);

    List<Custo> findByVeiculoIdVeiculoAndDataPagamentoBetween(Long idVeiculo, LocalDate inicioSemana, LocalDate hoje);
}
