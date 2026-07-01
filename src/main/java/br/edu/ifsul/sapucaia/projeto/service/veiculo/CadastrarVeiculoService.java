package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.CadastrarVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ProximaRevisaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.CadastrarVeiculoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.ia.IAService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioComVeiculoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaAnoVeiculoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoVeiculoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidarPlacaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.VeiculoMapper.toEntity;
import static br.edu.ifsul.sapucaia.projeto.mapper.VeiculoMapper.toResponse;

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

    private final ValidaUsuarioComVeiculoService validaUsuarioComVeiculoService;

    private final IAService iaService;

    ManutencaoIAResponse manutencaoIAResponse;

    public CadastrarVeiculoResponse cadastrar(CadastrarVeiculoRequest cadastrarVeiculoRequest) {

        validaVeiculoService.jaExistePlaca(cadastrarVeiculoRequest.getPlaca());
        validarPlacaValidator.formatoValido(cadastrarVeiculoRequest.getPlaca());
        validaTipoVeiculoValidator.tipoAceito(cadastrarVeiculoRequest.getTipo());
        validaAnoVeiculoValidator.anoMenorQueAtual(cadastrarVeiculoRequest.getAno());
        validaUsuarioService.porId(cadastrarVeiculoRequest.getIdUsuario());

        Usuario usuario = usuarioRepository.findById(cadastrarVeiculoRequest.getIdUsuario()).get();

        validaUsuarioComVeiculoService.porUsuario(usuario);

        Veiculo veiculo = toEntity(cadastrarVeiculoRequest);
        veiculo.setDataUltimaAtualizacaoKm(DateNow.now());

        veiculo.setUsuario(usuario);

        veiculo.setAtivo(true);

        try {
             manutencaoIAResponse = iaService.chamadaChat(cadastrarVeiculoRequest.getMarca() + " " +
                    cadastrarVeiculoRequest.getModelo() + " " +
                    cadastrarVeiculoRequest.getAno() + " " +
                    cadastrarVeiculoRequest.getKmAtual() + " km");

            veiculo.setProximaManutencaoKm(veiculo.getKmAtual() + manutencaoIAResponse.proximaRevisao().distanciaRestanteKm());
            veiculo.setProximaManutencaoData(veiculo.getDataUltimaAtualizacaoKm().plusMonths(manutencaoIAResponse.proximaRevisao().intervaloManutencoesMeses()));
        }
        catch(Exception e){
            manutencaoIAResponse = new ManutencaoIAResponse(
                    null,
                    new ProximaRevisaoIAResponse(0,0, 0)
            );
            veiculo.setProximaManutencaoKm(0);
            veiculo.setProximaManutencaoData(null);
        }

        veiculo.setIntervaloEntreManutencoesKm(manutencaoIAResponse.proximaRevisao().intervaloManutencoesKm());
        veiculo.setIntervaloEntreManutencoesMeses(manutencaoIAResponse.proximaRevisao().intervaloManutencoesMeses());

        veiculoRepository.save(veiculo);

        usuario.setVeiculo(veiculo);
        usuario.setPossuiVeiculo(true);

        usuarioRepository.save(usuario);

        return toResponse(veiculo);
    }
}
