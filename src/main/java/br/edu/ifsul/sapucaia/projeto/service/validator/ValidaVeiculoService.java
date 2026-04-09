package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
@RequiredArgsConstructor
@Service
public class ValidaVeiculoService {

    private final VeiculoRepository veiculoRepository;

    public void porId( Long idVeiculo) {

        if(!veiculoRepository.existsById(idVeiculo)){
            throw new ResponseStatusException(BAD_REQUEST, "Esse veículo não existe.");
        }
    }
}
