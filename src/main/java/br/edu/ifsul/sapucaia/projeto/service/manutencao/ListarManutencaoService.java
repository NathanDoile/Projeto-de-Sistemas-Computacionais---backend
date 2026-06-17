package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.controller.response.manutencao.ManutencaoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.mapper.ManutencaoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarManutencaoService {

    private final ManutencaoRepository manutencaoRepository;

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public Page<ManutencaoResponse> listar(Pageable pageable) {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        Page<Manutencao> manutencoes = manutencaoRepository.findAllByVeiculoUsuarioIdUsuarioAndIsAtivo(
                usuarioSecurity.getId(),
                true,
                pageable
        );

        return manutencoes.map(ManutencaoMapper::toManutencaoResponse);
    }
}