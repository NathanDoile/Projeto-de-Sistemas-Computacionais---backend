package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarNotificacaoManutencaoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AlterarNotificacaoManutencaoService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void alterarNotificacaoManutencao(Long id, AlterarNotificacaoManutencaoRequest request){

        Usuario usuario = usuarioRepository
                .findByIdUsuarioAndIsAtivo(id, true)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado."));

        usuario.setNotificacaoManutencao(request.getNotificacaoManutencao());

        usuarioRepository.save(usuario);
    }
}