package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaFormatoMetaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorMetaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.edu.ifsul.sapucaia.projeto.mapper.MetaMapper.toEntity;

@Service
@RequiredArgsConstructor
public class CadastrarMetaService {

    private final ValidaUsuarioService validaUsuarioService;

    private final UsuarioRepository usuarioRepository;

    private final MetaRepository metaRepository;

    private final ValidaValorMetaValidator validaValorMetaValidator;

    private final ValidaFormatoMetaValidator validaFormatoMetaValidator;
    
    @Transactional
    public void cadastrar(CadastrarMetaRequest cadastrarMetaRequest) {

        validaUsuarioService.porId(cadastrarMetaRequest.getIdUsuario());
        validaValorMetaValidator.isPositivo(cadastrarMetaRequest.getValor());
        validaFormatoMetaValidator.formatoValido(cadastrarMetaRequest.getFormato());

        Meta meta = toEntity(cadastrarMetaRequest);

        Usuario usuario = usuarioRepository.findById(cadastrarMetaRequest.getIdUsuario()).get();

        meta.setUsuario(usuario);

        meta.setAtivo(true);

        meta.setValorAtual(0);

        metaRepository.save(meta);
    }
}
