package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.PendenciasDoUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PendenciasDoUsuarioServiceTest {

    @InjectMocks
    private PendenciasDoUsuarioService tested;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Test
    @DisplayName("Deve retornar todas as pendências como verdadeiras quando tudo estiver vencido ou desatualizado")
    void deveRetornarPendenciasVerdadeirasQuandoTudoVencido() {

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Long id = usuarioLogado.getId();

        Veiculo veiculo = veiculo();
        veiculo.setDataUltimaAtualizacaoKm(LocalDate.now().minusDays(10));
        veiculo.setKmAtual(8000);
        veiculo.setProximaManutencaoKm(7500);
        veiculo.setProximaManutencaoData(LocalDate.now().minusDays(5));

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(veiculoRepository.findByUsuarioIdUsuario(id)).thenReturn(veiculo);

        PendenciasDoUsuarioResponse response = tested.buscarPendencias();

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuario(id);

        assertTrue(response.isKmDesatualizado());
        assertTrue(response.isManutencaoKmVencido());
        assertTrue(response.isManutencaoTempoVencido());
    }

    @Test
    @DisplayName("Deve retornar todas as pendências como falsas quando tudo estiver em dia")
    void deveRetornarPendenciasFalsasQuandoTudoEmDia() {

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Long id = usuarioLogado.getId();

        Veiculo veiculo = veiculo();
        veiculo.setDataUltimaAtualizacaoKm(LocalDate.now().minusDays(5));
        veiculo.setKmAtual(9000);
        veiculo.setProximaManutencaoKm(10000);
        veiculo.setProximaManutencaoData(LocalDate.now().plusDays(7));

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(veiculoRepository.findByUsuarioIdUsuario(id)).thenReturn(veiculo);

        PendenciasDoUsuarioResponse response = tested.buscarPendencias();

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuario(id);

        assertFalse(response.isKmDesatualizado());
        assertFalse(response.isManutencaoKmVencido());
        assertFalse(response.isManutencaoTempoVencido());
    }

    @Test
    @DisplayName("Deve marcar KM como desatualizado se a data da última atualização for nula")
    void deveMarcarKmDesatualizadoQuandoDataUltimaAtualizacaoNula() {

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Long id = usuarioLogado.getId();

        Veiculo veiculo = veiculo();
        veiculo.setDataUltimaAtualizacaoKm(null);
        veiculo.setKmAtual(9000);
        veiculo.setProximaManutencaoKm(10000);
        veiculo.setProximaManutencaoData(LocalDate.now().plusDays(5));

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(veiculoRepository.findByUsuarioIdUsuario(id)).thenReturn(veiculo);

        PendenciasDoUsuarioResponse response = tested.buscarPendencias();

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuario(id);

        assertTrue(response.isKmDesatualizado());
        assertFalse(response.isManutencaoKmVencido());
        assertFalse(response.isManutencaoTempoVencido());
    }

    @Test
    @DisplayName("Deve marcar manutenção por tempo como falsa se a próxima data for nula")
    void deveMarcarManutencaoTempoFalsaQuandoProximaDataNula() {

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Long id = usuarioLogado.getId();

        Veiculo veiculo = veiculo();
        veiculo.setDataUltimaAtualizacaoKm(LocalDate.now());
        veiculo.setKmAtual(9000);
        veiculo.setProximaManutencaoKm(10000);
        veiculo.setProximaManutencaoData(null);

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(veiculoRepository.findByUsuarioIdUsuario(id)).thenReturn(veiculo);

        PendenciasDoUsuarioResponse response = tested.buscarPendencias();

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuario(id);

        assertFalse(response.isKmDesatualizado());
        assertFalse(response.isManutencaoKmVencido());
        assertFalse(response.isManutencaoTempoVencido());
    }
}