package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.response.meta.BuscarMetaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.mapper.MetaMapper;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarMetasService {

    private final ValidaUsuarioService validaUsuarioService;

    private final MetaRepository metaRepository;

    public Page<BuscarMetaResponse> buscar(Long idUsuario, Pageable pageable) {

        validaUsuarioService.porId(idUsuario);

        Page<Meta> metas = metaRepository.findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true, pageable);

        return metas.map(MetaMapper::toResponse);
    }
}