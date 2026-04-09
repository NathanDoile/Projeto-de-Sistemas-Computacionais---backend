package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper.toEntity;

@RequiredArgsConstructor
@Service
public class CadastrarCustoService {

    private final ValidaVeiculoService validaVeiculoService;

    private final VeiculoRepository veiculoRepository;

    private final CustoRepository custoRepository;

    private final ValidaValorCustoValidator validaValorCustoValidator;

    @Transactional
    public void cadastrar(CadastrarCustoRequest request) {

        validaVeiculoService.porId(request.getIdVeiculo());
        validaValorCustoValidator.isPositivo(request.getValor());

        Custo custo = toEntity(request);
      
        Veiculo veiculo = veiculoRepository.findById(request.getIdVeiculo()).get();
        custo.setVeiculo(veiculo);

        custoRepository.save(custo);
    }
}
