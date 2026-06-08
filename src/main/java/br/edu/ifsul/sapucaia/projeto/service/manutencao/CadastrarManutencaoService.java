package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.manutencao.CadastrarManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.custo.CadastrarCustoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoPossuiManutencaoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoManutencaoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataManutencaoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.ManutencaoMapper.toEntity;

@Service
@RequiredArgsConstructor
public class CadastrarManutencaoService {

    private final ValidaTipoManutencaoValidator validaTipoManutencaoValidator;

    private final ValidaDataManutencaoValidator validadataManutencaoValidator;

    private final ValidaVeiculoService validaVeiculoService;

    private final ValidaCustoService validaCustoService;

    private final VeiculoRepository veiculoRepository;

    private final CustoRepository custoRepository;

    private final ManutencaoRepository manutencaoRepository;

    private final ValidaCustoPossuiManutencaoService validaCustoPossuiManutencaoService;

    private final CadastrarCustoService cadastrarCustoService;

    @Transactional
    public void cadastrar(CadastrarManutencaoRequest cadastrarManutencaoRequest) {

        validaTipoManutencaoValidator.tipoValido(cadastrarManutencaoRequest.getTipo());
        validadataManutencaoValidator.dataMenorQueHoje(cadastrarManutencaoRequest.getDataManutencao());
        validaVeiculoService.porId(cadastrarManutencaoRequest.getIdVeiculo());

        CadastrarCustoRequest cadastrarCustoRequest = CadastrarCustoRequest
                .builder()
                .idVeiculo(cadastrarManutencaoRequest.getIdVeiculo())
                .tipo(TipoCusto.MANUTENCAO.getDescricao())
                .valor(cadastrarManutencaoRequest.getValor())
                .dataPagamento(cadastrarManutencaoRequest.getDataManutencao())
                .descricao(cadastrarManutencaoRequest.getDescricao())
                .build();

        BuscarCustosEmAbertoResponse custoResponse = cadastrarCustoService.cadastrar(cadastrarCustoRequest);

        Manutencao manutencao = toEntity(cadastrarManutencaoRequest);

        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(cadastrarManutencaoRequest.getIdVeiculo(), true);

        Custo custo = custoRepository.findByIdCustoAndIsAtivo(custoResponse.getIdCusto(), true).get();

        manutencao.setVeiculo(veiculo);
        manutencao.setAtivo(true);
        manutencao.setCusto(custo);

        manutencaoRepository.save(manutencao);

        veiculo.getManutencoes().add(manutencao);

        veiculoRepository.save(veiculo);

        custo.setManutencao(manutencao);

        custoRepository.save(custo);

    }
}
