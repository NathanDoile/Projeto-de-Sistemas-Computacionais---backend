package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarCustosEmAbertoService {

    private final ValidaUsuarioService validaUsuarioService;

    private final CustoRepository custoRepository;

    private final VeiculoRepository veiculoRepository;

    private final UsuarioRepository usuarioRepository;

    public List<BuscarCustosEmAbertoResponse> buscar(Long id) {

        validaUsuarioService.porId(id);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(id, true).get();

        Veiculo veiculo = usuario.getVeiculo();

        List<Custo> custos = custoRepository.findByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true);

        return custos
                .stream()
                .map(CustoMapper::toResponse)
                .toList();
    }
}
