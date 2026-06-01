package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarCustosEmAbertoService {

    private final ValidaUsuarioService validaUsuarioService;

    private final CustoRepository custoRepository;

    private final UsuarioRepository usuarioRepository;

    public Page<BuscarCustosEmAbertoResponse> buscar(Long id, Pageable pageable) {

        validaUsuarioService.porId(id);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(id, true).get();

        Veiculo veiculo = usuario.getVeiculo();

        Page<Custo> custos = custoRepository.findAllByVeiculoAndDataPagamentoIsNullAndIsAtivo(veiculo, true, pageable);

        return custos.map(CustoMapper::toResponse);
    }
}
