package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.AtualizarKmVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
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

    @Transactional
    public void atualizar(@Valid AtualizarKmVeiculoRequest atualizarKmVeiculoRequest) {

        UsuarioSecurity usuario = usuarioAutenticadoService.getUser();

        Veiculo veiculo = veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true);

        validaKmAtualizadoVeiculoValidator.maiorQueAtual(atualizarKmVeiculoRequest.getKmAtualizado(), veiculo.getKmAtual());

        veiculo.setKmAtual(atualizarKmVeiculoRequest.getKmAtualizado());
        veiculo.setDataUltimaAtualizacaoKm(DateNow.now());
        veiculo.setProximaManutencaoData(DateNow.now().plusMonths(veiculo.getIntervaloEntreManutencoesMeses()));

        veiculoRepository.save(veiculo);
    }
}