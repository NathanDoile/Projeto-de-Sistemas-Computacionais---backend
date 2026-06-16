package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaFormatoMetaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorMetaValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.MetaMapper.toEntity;

@Service
@RequiredArgsConstructor
public class CadastrarMetaService {

    private final ValidaUsuarioService validaUsuarioService;
    private final UsuarioRepository usuarioRepository;
    private final MetaRepository metaRepository;
    private final ValidaValorMetaValidator validaValorMetaValidator;
    private final ValidaFormatoMetaValidator validaFormatoMetaValidator;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @Transactional
    public void cadastrar(CadastrarMetaRequest request) {

        validaValorMetaValidator.isPositivo(request.getValor());
        validaFormatoMetaValidator.formatoValido(request.getFormato());

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        validaUsuarioService.porId(usuarioSecurity.getId());

        Meta meta = toEntity(request);

        Usuario usuario = usuarioRepository
                .findByIdUsuarioAndIsAtivo(usuarioSecurity.getId(), true)
                .get();

        meta.setUsuario(usuario);
        meta.setAtivo(true);
        meta.setValorAtual(0);

        metaRepository.save(meta);
    }
}