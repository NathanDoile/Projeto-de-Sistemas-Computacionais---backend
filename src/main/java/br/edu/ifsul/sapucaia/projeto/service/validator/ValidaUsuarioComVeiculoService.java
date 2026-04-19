package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class ValidaUsuarioComVeiculoService {

    private final VeiculoRepository veiculoRepository;

    public void porUsuario(Usuario usuario) {

        if(veiculoRepository.existsByUsuario(usuario)){
            throw new ResponseStatusException(CONFLICT, "Usuário já possui veículo cadastrado");
        }
    }
}
