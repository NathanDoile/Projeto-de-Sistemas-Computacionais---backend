package br.edu.ifsul.sapucaia.projeto.security.repository;

import br.edu.ifsul.sapucaia.projeto.security.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
