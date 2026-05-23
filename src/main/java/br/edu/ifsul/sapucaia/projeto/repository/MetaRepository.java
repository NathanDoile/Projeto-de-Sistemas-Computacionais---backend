package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaRepository extends JpaRepository<Meta, Long> {


    List<Meta> findByUsuarioIdUsuarioAndIsAtivo(@NotNull Long idUsuario, boolean b);

    boolean existsByIdMetaAndIsAtivo(Long id, boolean b);

    Meta findByIdMetaAndIsAtivo(Long id, boolean b);
}
