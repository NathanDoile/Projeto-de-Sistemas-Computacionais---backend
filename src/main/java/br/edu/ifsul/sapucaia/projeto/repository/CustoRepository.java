package br.edu.ifsul.sapucaia.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;

public interface CustoRepository extends JpaRepository<Custo, Long> {

}
