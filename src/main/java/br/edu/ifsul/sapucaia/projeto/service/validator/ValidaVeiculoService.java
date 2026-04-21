package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ValidaVeiculoService {

    private final VeiculoRepository veiculoRepository;

    public void porId(Long idVeiculo) {

        if(!veiculoRepository.existsById(idVeiculo)){
            throw new ResponseStatusException(NOT_FOUND, "Veículo não encontrado.");
        }
    }

    public void jaExistePlaca(String placa) {

        if(veiculoRepository.existsByPlacaAndIsAtivo(placa, true)){
            throw new ResponseStatusException(CONFLICT, "Placa já cadastrada.");
        }
    }
}