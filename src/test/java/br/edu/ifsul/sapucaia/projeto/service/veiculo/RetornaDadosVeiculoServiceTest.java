package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.response.veiculo.RetornaDadosVeiculoResponse;
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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory.veiculo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetornaDadosVeiculoServiceTest {

    @InjectMocks
    private RetornaDadosVeiculoService tested;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Test
    @DisplayName("Retornar os dados do veículo")
    void retornaDadosVeiculoPossuiVeiculo(){

        UsuarioSecurity usuarioSecurity = usuarioSecurity();
        Veiculo veiculo = veiculo();
        veiculo.setIdVeiculo(usuarioSecurity.getIdVeiculo());

        Long id = veiculo.getIdVeiculo();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(veiculoRepository.findByIdVeiculo(id)).thenReturn(veiculo);

        RetornaDadosVeiculoResponse response = tested.dadosVeiculo();

        verify(usuarioAutenticadoService).getUser();
        verify(veiculoRepository).findByIdVeiculo(id);

        assertEquals(id, response.getIdVeiculo());
        assertEquals(veiculo.getModelo(), response.getModelo());
        assertEquals(veiculo.getDataUltimaAtualizacaoKm(), response.getDataUltimaAtualizacaoKm());
        assertEquals(veiculo.getMarca(), response.getMarca());
        assertEquals(veiculo.getPlaca(), response.getPlaca());
        assertEquals(veiculo.getTipo().toString(), response.getTipo());
        assertEquals(veiculo.getAno(), response.getAno());
        assertEquals(veiculo.getCor(), response.getCor());
        assertEquals(veiculo.getKmAtual(), response.getKmAtual());
        assertEquals(veiculo.getIntervaloEntreManutencoesKm(), response.getIntervaloEntreManutencoesKm());
        assertEquals(veiculo.getIntervaloEntreManutencoesMeses(), response.getIntervaloEntreManutencoesMeses());
        assertEquals(veiculo.getProximaManutencaoKm(), response.getProximaManutencaoKm());
        assertEquals(veiculo.getProximaManutencaoData(), response.getProximaManutencaoData());
    }
}
