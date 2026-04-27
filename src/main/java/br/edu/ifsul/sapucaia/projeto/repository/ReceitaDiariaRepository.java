package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReceitaDiariaRepository extends JpaRepository<ReceitaDiaria, Long> {

    List<ReceitaDiaria> findByUsuarioIdUsuarioAndDataReceitaBetween(Long idUsuario, LocalDate inicioSemana, LocalDate hoje);

    List<ReceitaDiaria> findByUsuarioAndDataBetween(Usuario usuario, LocalDate inicioSemana, LocalDate fimSemana);
}
