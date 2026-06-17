package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.PendenciasDoUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PendenciasDoUsuarioService {

    private final VeiculoRepository veiculoRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public PendenciasDoUsuarioResponse buscarPendencias() {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        Veiculo veiculo = veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuarioSecurity.getId(), true);

        LocalDate dataAtual = DateNow.now();

        boolean kmDesatualizado = false;
        if (veiculo.getDataUltimaAtualizacaoKm() != null) {
            LocalDate limiteParaAtualizar = veiculo.getDataUltimaAtualizacaoKm().plusDays(7);
            kmDesatualizado = dataAtual.isAfter(limiteParaAtualizar);
        } else {
            kmDesatualizado = true; 
        }

        boolean manutencaoKmVencido = veiculo.getKmAtual() >= veiculo.getProximaManutencaoKm();

        boolean manutencaoTempoVencido = false;
        if (veiculo.getProximaManutencaoData() != null) {
            manutencaoTempoVencido = dataAtual.isAfter(veiculo.getProximaManutencaoData());
        }

        return PendenciasDoUsuarioResponse.builder()
                .kmDesatualizado(kmDesatualizado)
                .manutencaoKmVencido(manutencaoKmVencido)
                .manutencaoTempoVencido(manutencaoTempoVencido)
                .build();
    }
}