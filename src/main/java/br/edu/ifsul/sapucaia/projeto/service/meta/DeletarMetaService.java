package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidarMetaService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeletarMetaService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final MetaRepository metaRepository;

    private final ValidarMetaService validarMetaService;

    @Transactional
    public void deletar(Long id) {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        validarMetaService.porId(id);

        Meta meta = metaRepository.findByUsuarioIdUsuarioAndUsuarioIsAtivoAndIdMetaAndIsAtivo(usuarioSecurity.getId(), true, id, true);

        meta.setAtivo(false);

        metaRepository.save(meta);
    }
}
