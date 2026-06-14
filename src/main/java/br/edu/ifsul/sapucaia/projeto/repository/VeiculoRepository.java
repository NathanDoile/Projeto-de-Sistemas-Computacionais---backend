package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    boolean existsByPlacaAndIsAtivo(String placa, boolean isAtivo);

    boolean existsByUsuario(Usuario usuario);

    boolean existsByIdVeiculoAndIsAtivo(Long id, boolean isAtivo);

    boolean existsByUsuarioIdUsuarioAndIsAtivo(Long idUsuario, boolean isAtivo);

    Veiculo findByIdVeiculoAndIsAtivo(Long id, boolean b);

    Veiculo findByUsuarioIdUsuarioAndIsAtivo(@NotNull Long idUsuario, boolean b);

    Veiculo findByUsuarioIdUsuarioAndUsuarioIsAtivoAndIdVeiculoAndIsAtivo(Long idUsuario, boolean b, Long id, boolean bo);
}
