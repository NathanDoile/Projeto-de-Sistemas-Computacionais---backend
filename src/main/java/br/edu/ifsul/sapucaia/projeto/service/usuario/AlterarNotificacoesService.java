package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarNotificacoesRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoNotificacao;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoNotificacaoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoNotificacao.MANUTENCAO;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AlterarNotificacoesService {
    private final UsuarioRepository usuarioRepository;

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final ValidaTipoNotificacaoValidator validaTipoNotificacaoValidator;

    @Transactional
    public void alterarNotificacoes(AlterarNotificacoesRequest alterarNotificacoesRequest) {

        UsuarioSecurity usuarioLogado = usuarioAutenticadoService.getUser();

        validaTipoNotificacaoValidator.tipoValido(alterarNotificacoesRequest.getNotificacao());

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(usuarioLogado.getId(), true)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado."));

        if (TipoNotificacao.deTexto(alterarNotificacoesRequest.getNotificacao()) == MANUTENCAO) {
            usuario.setNotificacaoManutencao(!usuario.isNotificacaoManutencao());
        } else {
            usuario.setNotificacaoVencimento(!usuario.isNotificacaoVencimento());
        }

        usuarioRepository.save(usuario);
    }
}