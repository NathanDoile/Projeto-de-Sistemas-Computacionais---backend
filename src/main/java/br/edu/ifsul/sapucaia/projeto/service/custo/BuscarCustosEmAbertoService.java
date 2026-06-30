package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarCustosEmAbertoService {

    private final CustoRepository custoRepository;

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final VeiculoRepository veiculoRepository;

    public Page<BuscarCustosEmAbertoResponse> buscar(Pageable pageable) {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        Veiculo veiculo = veiculoRepository.findByIdVeiculo(usuarioSecurity.getId());

        Page<Custo> custos = custoRepository.findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true, pageable);

        return custos.map(CustoMapper::toResponse);
    }
}
