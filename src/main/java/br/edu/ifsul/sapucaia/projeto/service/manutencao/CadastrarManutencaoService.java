package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoPossuiManutencaoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoManutencaoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidadataManutencaoValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.ManutencaoMapper.toEntity;

@Service
@RequiredArgsConstructor
public class CadastrarManutencaoService {

    private final ValidaTipoManutencaoValidator validaTipoManutencaoValidator;

    private final ValidadataManutencaoValidator validadataManutencaoValidator;

    private final ValidaVeiculoService validaVeiculoService;

    private final ValidaCustoService validaCustoService;

    private final VeiculoRepository veiculoRepository;

    private final CustoRepository custoRepository;

    private final ManutencaoRepository manutencaoRepository;

    private final ValidaCustoPossuiManutencaoService validaCustoPossuiManutencaoService;

    @Transactional
    public void cadastrar(@Valid CadastrarManutencaoRequest cadastrarManutencaoRequest) {

        validaTipoManutencaoValidator.tipoValido(cadastrarManutencaoRequest.getTipo());
        validadataManutencaoValidator.dataMenorQueHoje(cadastrarManutencaoRequest.getDataManutencao());
        validaVeiculoService.porId(cadastrarManutencaoRequest.getIdVeiculo());
        validaCustoService.porId(cadastrarManutencaoRequest.getIdCusto());
        validaCustoPossuiManutencaoService.porId(cadastrarManutencaoRequest.getIdCusto());

        Manutencao manutencao = toEntity(cadastrarManutencaoRequest);

        Veiculo veiculo = veiculoRepository.findById(cadastrarManutencaoRequest.getIdVeiculo()).get();
        Custo custo = custoRepository.findById(cadastrarManutencaoRequest.getIdCusto()).get();

        manutencao.setVeiculo(veiculo);
        manutencao.setCusto(custo);
        manutencao.setAtivo(true);

        manutencaoRepository.save(manutencao);

        veiculo.getManutencoes().add(manutencao);

        veiculoRepository.save(veiculo);

        custo.setManutencao(manutencao);

        custoRepository.save(custo);

    }
}
