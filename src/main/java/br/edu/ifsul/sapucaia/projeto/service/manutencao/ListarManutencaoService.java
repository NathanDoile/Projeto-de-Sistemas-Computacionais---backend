package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListarManutencaoService {

    private final ManutencaoRepository manutencaoRepository;

    private final ValidaUsuarioService validaUsuarioService;

    public Page<Manutencao> listar(Long idUsuario, Pageable pageable) {

        validaUsuarioService.porId(idUsuario);

        return manutencaoRepository
                .findAllByVeiculoUsuarioIdUsuarioAndIsAtivo(
                        idUsuario,
                        true,
                        pageable
                );
    }
}