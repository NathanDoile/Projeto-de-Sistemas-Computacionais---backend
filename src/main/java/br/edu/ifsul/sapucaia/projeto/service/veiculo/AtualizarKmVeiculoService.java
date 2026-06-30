package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.AtualizarKmVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ProximaRevisaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.service.ia.IAService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaKmAtualizadoVeiculoValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AtualizarKmVeiculoService {

    private final VeiculoRepository veiculoRepository;

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final ValidaKmAtualizadoVeiculoValidator validaKmAtualizadoVeiculoValidator;

    private final IAService iaService;

    @Transactional
    public void atualizar(@Valid AtualizarKmVeiculoRequest atualizarKmVeiculoRequest) {

        UsuarioSecurity usuario = usuarioAutenticadoService.getUser();

        Veiculo veiculo = veiculoRepository.findByUsuarioIdUsuario(usuario.getId());

        validaKmAtualizadoVeiculoValidator.maiorQueAtual(atualizarKmVeiculoRequest.getKmAtualizado(), veiculo.getKmAtual());

        veiculo.setKmAtual(atualizarKmVeiculoRequest.getKmAtualizado());
        veiculo.setDataUltimaAtualizacaoKm(DateNow.now());

        if(veiculo.getIntervaloEntreManutencoesKm() == 0) {
            ManutencaoIAResponse manutencaoIAResponse;
            try {
                manutencaoIAResponse = iaService.chamadaChat(veiculo.getMarca() + " " +
                        veiculo.getModelo() + " " +
                        veiculo.getAno() + " " +
                        veiculo.getKmAtual() + " km");

                veiculo.setProximaManutencaoKm(veiculo.getKmAtual() + manutencaoIAResponse.proximaRevisao().distanciaRestanteKm());
                veiculo.setProximaManutencaoData(veiculo.getDataUltimaAtualizacaoKm().plusMonths(manutencaoIAResponse.proximaRevisao().intervaloManutencoesMeses()));
            } catch (Exception e) {
                manutencaoIAResponse = new ManutencaoIAResponse(
                        null,
                        new ProximaRevisaoIAResponse(0, 0, 0)
                );
                veiculo.setProximaManutencaoKm(0);
                veiculo.setProximaManutencaoData(null);
            }

            veiculo.setIntervaloEntreManutencoesKm(manutencaoIAResponse.proximaRevisao().intervaloManutencoesKm());
            veiculo.setIntervaloEntreManutencoesMeses(manutencaoIAResponse.proximaRevisao().intervaloManutencoesMeses());
        }
        else{
            veiculo.setProximaManutencaoData(DateNow.now().plusMonths(veiculo.getIntervaloEntreManutencoesMeses()));
        }

        veiculoRepository.save(veiculo);
    }
}