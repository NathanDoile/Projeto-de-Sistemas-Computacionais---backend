package br.edu.ifsul.sapucaia.projeto.service.receita_diaria;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataReceitaDiariaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorReceitaDiariaValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.ReceitaDiariaFactory.cadastrarReceitaDiariaRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarReceitaDiariaServiceTest {

    @InjectMocks
    private CadastrarReceitaDiariaService tested;

    @Mock private ValidaUsuarioService validaUsuarioService;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ReceitaDiariaRepository receitaDiariaRepository;
    @Mock private ValidaValorReceitaDiariaValidator validaValorReceitaDiariaValidator;
    @Mock private ValidaDataReceitaDiariaValidator validaDataReceitaDiariaValidator;
    @Mock private UsuarioAutenticadoService usuarioAutenticadoService;

    private void mockAuth(CadastrarReceitaDiariaRequest request) {

        UsuarioSecurity security = mock(UsuarioSecurity.class);

        when(usuarioAutenticadoService.getUser()).thenReturn(security);
        when(security.getId()).thenReturn(request.getIdUsuario());
    }

    private void mockUser(Usuario usuario) {
        when(usuarioRepository.findByIdUsuarioAndIsAtivo(anyLong(), eq(true)))
                .thenReturn(Optional.of(usuario));
    }

    @Test
    void deveCadastrarReceitaDiariaCorretamente() {

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();
        Usuario usuario = usuario();

        mockAuth(request);
        mockUser(usuario);

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorReceitaDiariaValidator).isPositivo(request.getValor());
        verify(validaDataReceitaDiariaValidator).naoMaiorQueHoje(request.getDataReceita());

        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(request.getIdUsuario(), true);
        verify(receitaDiariaRepository).save(any());
    }

    @Test
    void naoDeveCadastrarSeUsuarioIncorreto() {

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        mockAuth(request);

        doThrow(ResponseStatusException.class)
                .when(validaUsuarioService).porId(request.getIdUsuario());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(receitaDiariaRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarSeValorInvalido() {

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        mockAuth(request);

        doThrow(ResponseStatusException.class)
                .when(validaValorReceitaDiariaValidator)
                .isPositivo(request.getValor());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(receitaDiariaRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarSeDataInvalida() {

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        mockAuth(request);

        doThrow(ResponseStatusException.class)
                .when(validaDataReceitaDiariaValidator)
                .naoMaiorQueHoje(request.getDataReceita());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(receitaDiariaRepository, never()).save(any());
    }
}