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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.AdministradorMapper.toEntity;

@Service
@RequiredArgsConstructor
public class CadastrarReceitaDiariaService {

    private final ValidaUsuarioService validaUsuarioService;

    private final UsuarioRepository usuarioRepository;

    private final ReceitaDiariaRepository receitaDiariaRepository;

    private final ValidaValorReceitaDiariaValidator validaValorReceitaDiariaValidator;

    private final ValidaDataReceitaDiariaValidator validaDataReceitaDiariaValidator;

    @Transactional
    public void cadastrar(CadastrarReceitaDiariaRequest cadastrarReceitaDiariaRequest) {

        validaUsuarioService.porId(cadastrarReceitaDiariaRequest.getIdUsuario());
        validaValorReceitaDiariaValidator.isPositivo(cadastrarReceitaDiariaRequest.getValor());
        validaDataReceitaDiariaValidator.naoMaiorQueHoje(cadastrarReceitaDiariaRequest.getDataReceita());

        ReceitaDiaria receitaDiaria = toEntity(cadastrarReceitaDiariaRequest);

        Usuario usuario = usuarioRepository.findById(cadastrarReceitaDiariaRequest.getIdUsuario()).get();

        receitaDiaria.setUsuario(usuario);

        receitaDiaria.setAtivo(true);

        receitaDiariaRepository.save(receitaDiaria);
    }
}
