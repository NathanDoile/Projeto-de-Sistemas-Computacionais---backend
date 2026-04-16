package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ValidaCustoService {

    private final CustoRepository custoRepository;


    public void porId(@NotNull Long idCusto) {

        if(!custoRepository.existsByIdCustoAndIsAtivo(idCusto, true)){
            throw new ResponseStatusException(NOT_FOUND, "Id do custo não encontrado.");
        }
    }
}
