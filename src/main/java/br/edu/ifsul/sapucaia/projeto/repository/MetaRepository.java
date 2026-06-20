package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetaRepository extends JpaRepository<Meta, Long> {

    Page<Meta> findByUsuarioIdUsuarioAndIsAtivo(@NotNull Long idUsuario, boolean isAtivo, Pageable pageable);

    boolean existsByIdMetaAndIsAtivo(Long id, boolean b);

    Meta findByIdMetaAndIsAtivo(Long id, boolean b);

    Meta findByUsuarioIdUsuarioAndIdMetaAndIsAtivo(Long idUsuario, Long id, boolean b);
}