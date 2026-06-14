package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.response.meta.BuscarMetaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.mapper.MetaMapper;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarMetasService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final MetaRepository metaRepository;

    public Page<BuscarMetaResponse> buscar(Pageable pageable) {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        Page<Meta> metas = metaRepository.findByUsuarioIdUsuarioAndIsAtivo(usuarioSecurity.getId(), true, pageable);

        return metas.map(MetaMapper::toResponse);
    }
}