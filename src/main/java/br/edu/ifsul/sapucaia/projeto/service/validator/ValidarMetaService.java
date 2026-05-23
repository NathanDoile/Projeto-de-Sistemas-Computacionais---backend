package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@AllArgsConstructor
public class ValidarMetaService {

    private final MetaRepository metaRepository;

    public void porId(Long id){

        if(!metaRepository.existsByIdMetaAndIsAtivo(id, true)){
            throw new ResponseStatusException(NOT_FOUND, "Não existe meta com esse ID");
        }
    }
}
