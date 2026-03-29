package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaDiariaRepository extends JpaRepository<ReceitaDiaria, Long> {
}
