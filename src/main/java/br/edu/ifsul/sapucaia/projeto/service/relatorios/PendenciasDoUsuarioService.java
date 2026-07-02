package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.PendenciasDoUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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

        Veiculo veiculo = veiculoRepository
                .findByUsuarioIdUsuario(usuarioSecurity.getId());

        LocalDate dataAtual = DateNow.now();

        boolean kmDesatualizado = veiculo.getDataUltimaAtualizacaoKm() == null
                || dataAtual.isAfter(veiculo.getDataUltimaAtualizacaoKm().plusDays(7));

        boolean manutencaoKmVencido = veiculo.getProximaManutencaoKm() != 0
                && veiculo.getKmAtual() >= veiculo.getProximaManutencaoKm();

        boolean manutencaoTempoVencido =
                veiculo.getProximaManutencaoData() != null
                        && dataAtual.isAfter(veiculo.getProximaManutencaoData());

        return PendenciasDoUsuarioResponse.builder()
                .kmDesatualizado(kmDesatualizado)
                .manutencaoKmVencido(manutencaoKmVencido)
                .manutencaoTempoVencido(manutencaoTempoVencido)
                .build();
    }
}