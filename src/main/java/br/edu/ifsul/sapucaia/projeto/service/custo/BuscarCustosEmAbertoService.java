package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarCustosEmAbertoService {

    private final ValidaVeiculoService validaVeiculoService;

    private final CustoRepository custoRepository;

    private final VeiculoRepository veiculoRepository;

    public List<BuscarCustosEmAbertoResponse> buscar(Long id) {

        validaVeiculoService.porId(id);

        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(id, true);

        List<Custo> custos = custoRepository.findByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true);

        return custos
                .stream()
                .map(CustoMapper::toResponse)
                .toList();
    }
}
