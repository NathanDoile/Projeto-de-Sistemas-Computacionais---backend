package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByIdUsuarioAndIsAtivo(Long idUsuario, boolean isAtivo);

    boolean existsByEmail(String email);

    boolean existsByTelefone(String telefone, boolean isAtivo);

    boolean existsByEmailAndIdUsuarioNot(String email, Long idUsuario, boolean isAtivo);

    boolean existsByTelefoneAndIdUsuarioNot(String telefone, Long idUsuario, boolean isAtivo);

    Optional<Usuario> findByEmail(String email);

    boolean existsByIdUsuarioAndSenhaAndIsAtivo(Long id, String senhaAtual, boolean isAtivo);

    boolean existsByIdUsuarioAndIsAtivo(Long idUsuario, boolean isAtivo);

    boolean existsByIdUsuarioAndSenhaAndIsAtivoTrue(Long idUsuario, String senha);
}
