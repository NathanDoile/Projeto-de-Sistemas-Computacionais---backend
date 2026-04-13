package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByTelefone(String telefone);

    boolean existsByEmailAndIdUsuarioNot(String email, Long idUsuario);

    boolean existsByTelefoneAndIdUsuarioNot(String telefone, Long idUsuario);
    
}
