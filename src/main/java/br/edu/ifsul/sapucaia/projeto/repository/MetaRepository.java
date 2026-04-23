package br.edu.ifsul.sapucaia.projeto.repository;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.SEMANAL;

public interface MetaRepository extends JpaRepository<Meta, Long> {

    static CadastrarMetaRequest cadastrarMetaRequest() {

        return CadastrarMetaRequest
                .builder()
                .titulo("Meta Teste")
                .valor(200.00)
                .formato(SEMANAL.toString())
                .idUsuario(1L)
                .build();
    }
}
