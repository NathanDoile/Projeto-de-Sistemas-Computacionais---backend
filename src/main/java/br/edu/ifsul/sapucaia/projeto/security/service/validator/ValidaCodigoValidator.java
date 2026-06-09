package br.edu.ifsul.sapucaia.projeto.security.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ValidaCodigoValidator {

    public static final String MSG_CODIGO_INVALIDO = "Código inválido.";

    public static final String MSG_SEM_CODIGO = "Nenhum código de redefinição de senha foi gerado para este usuário.";

    private final UsuarioRepository usuarioRepository;

    public void validarCodigo(Usuario usuario, String codigo) {

        if (usuario.getCodigoRedefinirSenha() == null) {

            throw new ResponseStatusException(BAD_REQUEST, MSG_SEM_CODIGO);

        }

        if (!usuario.getCodigoRedefinirSenha().equals(codigo)) {

            usuario.setTentativasRedefinirSenha(usuario.getTentativasRedefinirSenha() + 1);

            usuarioRepository.save(usuario);

            throw new ResponseStatusException(BAD_REQUEST, MSG_CODIGO_INVALIDO);
            
        }

    }

}