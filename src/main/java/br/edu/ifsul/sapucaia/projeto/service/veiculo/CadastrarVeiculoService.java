package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.CadastrarVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.mapper.VeiculoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaAnoVeiculoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoVeiculoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidarPlacaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static br.edu.ifsul.sapucaia.projeto.mapper.VeiculoMapper.toEntity;
import static java.time.LocalDate.now;

@Service
@RequiredArgsConstructor
public class CadastrarVeiculoService {

    private final ValidarPlacaValidator validarPlacaValidator;

    private final ValidaVeiculoService validaVeiculoService;

    private final ValidaTipoVeiculoValidator validaTipoVeiculoValidator;

    private final ValidaAnoVeiculoValidator validaAnoVeiculoValidator;

    private final ValidaUsuarioService validaUsuarioService;

    private final UsuarioRepository usuarioRepository;

    private final VeiculoRepository veiculoRepository;

    public void cadastrar(CadastrarVeiculoRequest cadastrarVeiculoRequest) {


        validaVeiculoService.jaExistePlaca(cadastrarVeiculoRequest.getPlaca());
        validarPlacaValidator.formatoValido(cadastrarVeiculoRequest.getPlaca());
        validaTipoVeiculoValidator.tipoAceito(cadastrarVeiculoRequest.getTipo());
        validaAnoVeiculoValidator.anoMenorQueAtual(cadastrarVeiculoRequest.getAno());
        validaUsuarioService.porId(cadastrarVeiculoRequest.getIdUsuario());

        Veiculo veiculo = toEntity(cadastrarVeiculoRequest);
        veiculo.setDataUltimaAtualizacaoKm(now());

        Usuario usuario = usuarioRepository.findById(cadastrarVeiculoRequest.getIdUsuario()).get();

        veiculo.setUsuario(usuario);

        veiculoRepository.save(veiculo);

        usuario.setVeiculo(veiculo);

        usuarioRepository.save(usuario);
    }
}
