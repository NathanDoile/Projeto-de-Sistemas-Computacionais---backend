package br.edu.ifsul.sapucaia.projeto.service.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaFormatoMetaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorMetaValidator;

import static br.edu.ifsul.sapucaia.projeto.mapper.MetaMapper.toEntity;

@Service
public class CadastrarMetaService {

    @Autowired
    private ValidaUsuarioService validaUsuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private ValidaValorMetaValidator validaValorMetaValidator;

    @Autowired
    private ValidaFormatoMetaValidator validaFormatoMetaValidator;
    
    @Transactional
    public void cadastrar(CadastrarMetaRequest cadastrarMetaRequest) {

        validaUsuarioService.porId(cadastrarMetaRequest.getIdUsuario());
        validaValorMetaValidator.isPositivo(cadastrarMetaRequest.getValor());
        validaFormatoMetaValidator.formatoValido(cadastrarMetaRequest.getFormato());

        Meta meta = toEntity(cadastrarMetaRequest);

        Usuario usuario = usuarioRepository.findById(cadastrarMetaRequest.getIdUsuario()).get();

        meta.setUsuario(usuario);

        metaRepository.save(meta);
    }
}
