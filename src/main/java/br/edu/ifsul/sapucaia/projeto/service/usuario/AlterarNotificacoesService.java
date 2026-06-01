package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarNotificacoesRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoNotificacao;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoNotificacaoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoNotificacao.MANUTENCAO;

@Service
@RequiredArgsConstructor
public class AlterarNotificacoesService {
    private final UsuarioRepository usuarioRepository;

    private final ValidaUsuarioService validaUsuarioService;

    private final ValidaTipoNotificacaoValidator validaTipoNotificacaoValidator;

    @Transactional
    public void alterarNotificacoes(Long id, AlterarNotificacoesRequest alterarNotificacoesRequest){
        validaUsuarioService.porId(id);
        validaTipoNotificacaoValidator.tipoValido(alterarNotificacoesRequest.getNotificacao());
        Usuario usuario = usuarioRepository.findById(id).get();

        if(TipoNotificacao.deTexto(alterarNotificacoesRequest.getNotificacao()) == MANUTENCAO){
            usuario.setNotificacaoManutencao(!usuario.isNotificacaoManutencao());
        }
        else{
           usuario.setNotificacaoVencimento(!usuario.isNotificacaoVencimento());
        }

        usuarioRepository.save(usuario);
    }
}
