package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.AtualizarKmVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.ia.ManutencaoIAResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.factory.IAFactory;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.ia.IAService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaKmAtualizadoVeiculoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.atualizarKmVeiculoRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarKmVeiculoServiceTest {

    @InjectMocks
    private AtualizarKmVeiculoService tested;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private ValidaKmAtualizadoVeiculoValidator validaKmAtualizadoVeiculoValidator;

    @Mock
    private IAService iaService;

    @Captor
    private ArgumentCaptor<Veiculo> veiculoCaptor;

    @Test
    @DisplayName("Deve atualizar km do veículo corretamente - não chama é, informações de próximas manutenções diferentes de 0")
    void deveAtualizarKmVeiculoCorretamenteNaoChamaIA() {

        AtualizarKmVeiculoRequest request = atualizarKmVeiculoRequest();
        UsuarioSecurity usuario = usuarioSecurity();
        Veiculo veiculo = veiculo();
        veiculo.setIntervaloEntreManutencoesKm(5000);
        veiculo.setIntervaloEntreManutencoesMeses(6);
        veiculo.setProximaManutencaoKm(20000);
        veiculo.setProximaManutencaoData(veiculo.getDataUltimaAtualizacaoKm().plusMonths(6));

        int kmAtualizado = (int) request.getKmAtualizado();
        int kmAtual = (int) veiculo.getKmAtual();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuario);
        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true)).thenReturn(veiculo);

        tested.atualizar(request);

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true);
        verify(validaKmAtualizadoVeiculoValidator).maiorQueAtual(kmAtualizado, kmAtual);
        verify(iaService, never()).chamadaChat(anyString());
        verify(veiculoRepository).save(veiculoCaptor.capture());

        Veiculo response = veiculoCaptor.getValue();
        assertEquals(request.getKmAtualizado(), response.getKmAtual());
        assertEquals(response.getDataUltimaAtualizacaoKm().plusMonths(veiculo.getIntervaloEntreManutencoesMeses()), response.getProximaManutencaoData());
        assertEquals(5000, response.getIntervaloEntreManutencoesKm());
        assertEquals(6, response.getIntervaloEntreManutencoesMeses());
    }

    @Test
    @DisplayName("Deve atualizar km do veículo corretamente - chama IA, pois km entre manutenções é 0")
    void deveAtualizarKmVeiculoCorretamenteChamaIA() {

        AtualizarKmVeiculoRequest request = atualizarKmVeiculoRequest();
        UsuarioSecurity usuario = usuarioSecurity();
        Veiculo veiculo = veiculo();

        int kmAtualizado = (int) request.getKmAtualizado();
        int kmAtual = (int) veiculo.getKmAtual();

        ManutencaoIAResponse iaresponse = IAFactory.response();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuario);
        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true)).thenReturn(veiculo);
        when(iaService.chamadaChat(anyString())).thenReturn(iaresponse);

        tested.atualizar(request);

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true);
        verify(validaKmAtualizadoVeiculoValidator).maiorQueAtual(kmAtualizado, kmAtual);
        verify(iaService).chamadaChat(anyString());
        verify(veiculoRepository).save(veiculoCaptor.capture());

        Veiculo response = veiculoCaptor.getValue();
        assertEquals(request.getKmAtualizado(), response.getKmAtual());
        assertEquals(response.getIntervaloEntreManutencoesKm(), iaresponse.proximaRevisao().intervaloManutencoesKm());
        assertEquals(response.getIntervaloEntreManutencoesMeses(), iaresponse.proximaRevisao().intervaloManutencoesMeses());
        assertEquals(response.getProximaManutencaoKm(), request.getKmAtualizado() + iaresponse.proximaRevisao().distanciaRestanteKm());
        assertEquals(response.getProximaManutencaoData(), response.getDataUltimaAtualizacaoKm().plusMonths(iaresponse.proximaRevisao().intervaloManutencoesMeses()));
        
    }

    @Test
    @DisplayName("Deve atualizar km do veículo corretamente - chama IA, pois km entre manutenções é 0, mas erro na IA")
    void deveAtualizarKmVeiculoCorretamenteChamaIAComErro() {

        AtualizarKmVeiculoRequest request = atualizarKmVeiculoRequest();
        UsuarioSecurity usuario = usuarioSecurity();
        Veiculo veiculo = veiculo();

        int kmAtualizado = (int) request.getKmAtualizado();
        int kmAtual = (int) veiculo.getKmAtual();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuario);
        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true)).thenReturn(veiculo);
        when(iaService.chamadaChat(anyString())).thenThrow(new RuntimeException("IA indisponível"));

        tested.atualizar(request);

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true);
        verify(validaKmAtualizadoVeiculoValidator).maiorQueAtual(kmAtualizado, kmAtual);
        verify(iaService).chamadaChat(anyString());
        verify(veiculoRepository).save(veiculoCaptor.capture());

        Veiculo response = veiculoCaptor.getValue();
        assertEquals(request.getKmAtualizado(), response.getKmAtual());
        assertEquals(0, response.getIntervaloEntreManutencoesKm());
        assertEquals(0, response.getIntervaloEntreManutencoesMeses());
        assertEquals(0, response.getProximaManutencaoKm());
        assertNull(response.getProximaManutencaoData());

    }

    @Test
    @DisplayName("Não deve atualizar km do veiculo se km atualizado não for maior que o atual")
    void naoDeveAtualizarKmVeiculoSeKmAtualizadoNaoMaiorQueAtual() {

        AtualizarKmVeiculoRequest request = atualizarKmVeiculoRequest();
        UsuarioSecurity usuario = usuarioSecurity();
        Veiculo veiculo = veiculo();

        int kmAtualizado = (int) request.getKmAtualizado();
        int kmAtual = (int) veiculo.getKmAtual();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuario);
        when(veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true)).thenReturn(veiculo);
        
        doThrow(ResponseStatusException.class)
                .when(validaKmAtualizadoVeiculoValidator).maiorQueAtual(kmAtualizado, kmAtual);

        assertThrows(ResponseStatusException.class, () -> tested.atualizar(request));

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByUsuarioIdUsuarioAndIsAtivo(usuario.getId(), true);
        verify(validaKmAtualizadoVeiculoValidator).maiorQueAtual(kmAtualizado, kmAtual);
        verify(iaService, never()).chamadaChat(anyString());
        verify(veiculoRepository, never()).save(any(Veiculo.class));

    }
}