package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    boolean existsByPlacaAndIsAtivo(String placa, boolean isAtivo);

    boolean existsByUsuario(Usuario usuario);

    boolean existsByIdVeiculoAndIsAtivo(Long id, boolean isAtivo);

    Veiculo findByIdVeiculoAndIsAtivo(Long id, boolean b);
}
