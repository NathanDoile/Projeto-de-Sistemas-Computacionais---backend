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
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.custo.CadastrarCustoService;
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

    private final VeiculoRepository veiculoRepository;

    private final CustoRepository custoRepository;

    private final ManutencaoRepository manutencaoRepository;

    private final CadastrarCustoService cadastrarCustoService;

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @Transactional
    public void cadastrar(CadastrarManutencaoRequest cadastrarManutencaoRequest) {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        validaTipoManutencaoValidator.tipoValido(cadastrarManutencaoRequest.getTipo());
        validadataManutencaoValidator.dataMenorQueHoje(cadastrarManutencaoRequest.getDataManutencao());
        validaVeiculoService.porIdUsuario(usuarioSecurity.getId());

        CadastrarCustoRequest cadastrarCustoRequest = CadastrarCustoRequest
                .builder()
                .tipo(TipoCusto.MANUTENCAO.getDescricao())
                .valor(cadastrarManutencaoRequest.getValor())
                .dataPagamento(cadastrarManutencaoRequest.getDataManutencao())
                .descricao(cadastrarManutencaoRequest.getDescricao())
                .build();

        BuscarCustosEmAbertoResponse custoResponse =
                cadastrarCustoService.cadastrar(cadastrarCustoRequest);

        Manutencao manutencao = toEntity(cadastrarManutencaoRequest);

        Veiculo veiculo = veiculoRepository
                .findByUsuarioIdUsuarioAndIsAtivo(usuarioSecurity.getId(), true);

        Custo custo = custoRepository
                .findByIdCustoAndIsAtivo(custoResponse.getIdCusto(), true)
                .get();

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