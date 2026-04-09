package br.edu.ifsul.sapucaia.projeto.service.custo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;

import static br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper.toEntity;

@Service
public class CadastrarCustoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private CustoRepository custoRepository;

    @Autowired
    private ValidaValorCustoValidator validaValorCustoValidator;

    @Transactional
    public void cadastrar(CadastrarCustoRequest cadastrarCustoRequest) {

        // validação
        validaValorCustoValidator.isPositivo(cadastrarCustoRequest.getValor());

        // mapeamento
        Custo custo = toEntity(cadastrarCustoRequest);

        // busca do veículo com tratamento de erro
        Veiculo veiculo = veiculoRepository
                .findById(cadastrarCustoRequest.getIdVeiculo())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        // associação
        custo.setVeiculo(veiculo);

        // persistência
        custoRepository.save(custo);

        veiculo.getCustos().add(custo);

        veiculoRepository.save(veiculo);
    }
}