package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper.toEntity;

@Service
public class CadastrarCustoService {

    @Autowired
    private ValidaVeiculoService validaVeiculoService;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private CustoRepository custoRepository;

    @Autowired
    private ValidaValorCustoValidator validaValorCustoValidator;

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
