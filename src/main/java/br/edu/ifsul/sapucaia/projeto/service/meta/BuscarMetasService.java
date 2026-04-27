package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.BuscarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.meta.BuscarMetaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.mapper.MetaMapper;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarMetasService {

    private final ValidaUsuarioService validaUsuarioService;

    private final MetaRepository metaRepository;

    public List<BuscarMetaResponse> buscar(BuscarMetaRequest request) {

        validaUsuarioService.porId(request.getIdUsuario());

        List<Meta> metas = metaRepository.findByUsuarioIdUsuarioAndIsAtivo(request.getIdUsuario(), true);

        return metas
                .stream()
                .map(MetaMapper::toResponse)
                .toList();
    }
}
