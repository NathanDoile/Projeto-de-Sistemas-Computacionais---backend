package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.RetornaDadosVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.VeiculoMapper.toDadosVeiculoResponse;


@Service
@RequiredArgsConstructor
public class RetornaDadosVeiculoService {
    private final ValidaVeiculoService validaVeiculoService;

    private final VeiculoRepository veiculoRepository;

    public RetornaDadosVeiculoResponse dadosVeiculo(Long id){
        validaVeiculoService.porId(id);

        validaVeiculoService.estaAtivo(id);

        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(id, true);

        return toDadosVeiculoResponse(veiculo);
    }
}