package br.edu.ifsul.sapucaia.projeto.service.receita_diaria;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataReceitaDiariaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorReceitaDiariaValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.AdministradorMapper.toEntity;

@Service
public class CadastrarReceitaDiariaService {

    @Autowired
    private ValidaUsuarioService validaUsuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Autowired
    private ValidaValorReceitaDiariaValidator validaValorReceitaDiariaValidator;

    @Autowired
    private ValidaDataReceitaDiariaValidator validaDataReceitaDiariaValidator;

    @Transactional
    public void cadastrar(CadastrarReceitaDiariaRequest cadastrarReceitaDiariaRequest) {

        validaUsuarioService.porId(cadastrarReceitaDiariaRequest.getIdUsuario());
        validaValorReceitaDiariaValidator.isPositivo(cadastrarReceitaDiariaRequest.getValor());
        validaDataReceitaDiariaValidator.naoMaiorQueHoje(cadastrarReceitaDiariaRequest.getDataReceita());

        ReceitaDiaria receitaDiaria = toEntity(cadastrarReceitaDiariaRequest);

        Usuario usuario = usuarioRepository.findById(cadastrarReceitaDiariaRequest.getIdUsuario()).get();

        receitaDiaria.setUsuario(usuario);

        receitaDiariaRepository.save(receitaDiaria);
    }
}
