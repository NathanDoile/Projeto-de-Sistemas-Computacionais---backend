package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.CadastrarVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.factory.VeiculoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioComVeiculoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaAnoVeiculoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoVeiculoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidarPlacaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarVeiculoServiceTest {

    @InjectMocks
    private CadastrarVeiculoService tested;

    @Mock
    private ValidarPlacaValidator validarPlacaValidator;

    @Mock
    private ValidaVeiculoService validaVeiculoService;

    @Mock
    private ValidaTipoVeiculoValidator validaTipoVeiculoValidator;

    @Mock
    private ValidaAnoVeiculoValidator validaAnoVeiculoValidator;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ValidaUsuarioComVeiculoService validaUsuarioComVeiculoService;

    @Captor
    private ArgumentCaptor<Veiculo> veiculoCaptor;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve cadastrar veiculo corretamente")
    void deveCadastrarVeiculoCorretamente(){

        CadastrarVeiculoRequest request = VeiculoFactory.cadastrarVeiculoRequest();

        Usuario usuario = usuario();

        when(usuarioRepository.findById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaVeiculoService).jaExistePlaca(request.getPlaca());
        verify(validarPlacaValidator).formatoValido(request.getPlaca());
        verify(validaTipoVeiculoValidator).tipoAceito(request.getTipo());
        verify(validaAnoVeiculoValidator).anoMenorQueAtual(request.getAno());
        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(usuarioRepository).findById(usuario.getIdUsuario());
        verify(validaUsuarioComVeiculoService).porUsuario(usuario);
        verify(veiculoRepository).save(veiculoCaptor.capture());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Veiculo veiculoResponse = veiculoCaptor.getValue();
        Usuario usuarioResponse = usuarioCaptor.getValue();

        assertEquals(request.getPlaca(), veiculoResponse.getPlaca());
        assertEquals(request.getTipo().toUpperCase(), veiculoResponse.getTipo().name());
        assertEquals(request.getCor(), veiculoResponse.getCor());
        assertEquals(request.getAno(), veiculoResponse.getAno());
        assertEquals(request.getMarca(), veiculoResponse.getMarca());
        assertEquals(request.getModelo(), veiculoResponse.getModelo());
        assertEquals(request.getIdUsuario(), veiculoResponse.getUsuario().getIdUsuario());
        assertEquals(LocalDate.now(), veiculoResponse.getDataUltimaAtualizacaoKm());
        assertTrue(veiculoResponse.isAtivo());
        assertEquals(usuario, veiculoResponse.getUsuario());
        assertEquals(veiculoResponse, usuarioResponse.getVeiculo());
        assertTrue(usuarioResponse.isPossuiVeiculo());
    }
}
