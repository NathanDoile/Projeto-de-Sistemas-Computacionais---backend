package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.RetornaDadosVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.VeiculoMapper.toDadosVeiculoResponse;


@Service
@RequiredArgsConstructor
public class RetornaDadosVeiculoService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final VeiculoRepository veiculoRepository;

    public RetornaDadosVeiculoResponse dadosVeiculo(){
        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        Veiculo veiculo = veiculoRepository.findByIdVeiculo(usuarioSecurity.getIdVeiculo());

        return toDadosVeiculoResponse(veiculo);
    }
}