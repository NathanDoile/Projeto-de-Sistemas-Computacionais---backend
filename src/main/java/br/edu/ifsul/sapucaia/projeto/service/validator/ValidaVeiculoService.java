package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Service
public class ValidaVeiculoService {

    private final VeiculoRepository veiculoRepository;

    public void porId(Long idVeiculo) {

        if(!veiculoRepository.existsByIdVeiculoAndIsAtivo(idVeiculo, true)){
            throw new ResponseStatusException(NOT_FOUND, "ID do veículo não existe.");
        }
    }

    public void porIdUsuario(Long idUsuario){
        if(!veiculoRepository.existsByUsuarioIdUsuarioAndIsAtivo(idUsuario, true)){
            throw new ResponseStatusException(NOT_FOUND, "Usuário sem veículo.");
        }
    }

    public void jaExistePlaca(String placa) {

        if(veiculoRepository.existsByPlacaAndIsAtivo(placa, true)){
            throw new ResponseStatusException(CONFLICT, "Placa já cadastrada.");
        }
    }

    public void estaAtivo(Long idVeiculo){
        if(!veiculoRepository.existsByIdVeiculoAndIsAtivo(idVeiculo, true)){
            throw new ResponseStatusException(CONFLICT, "Veículo não está ativo.");
        }
    }
}