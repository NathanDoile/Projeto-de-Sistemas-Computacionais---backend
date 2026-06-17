package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaFormatoMetaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorMetaValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.cadastrarMetaRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarMetaServiceTest {

    @InjectMocks
    private CadastrarMetaService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MetaRepository metaRepository;

    @Mock
    private ValidaValorMetaValidator validaValorMetaValidator;

    @Mock
    private ValidaFormatoMetaValidator validaFormatoMetaValidator;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private UsuarioSecurity usuarioSecurity;

    @Captor
    private ArgumentCaptor<Meta> metaCaptor;

    private void mockAuthBasic() {
        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(usuarioSecurity.getId()).thenReturn(1L);
    }

    private void mockUsuario() {
        when(usuarioRepository.findByIdUsuarioAndIsAtivo(1L, true))
                .thenReturn(Optional.of(usuario()));
    }

    @Test
    @DisplayName("Deve cadastrar meta corretamente")
    void cadastraMetaComValoresCorretos() {

        CadastrarMetaRequest request = cadastrarMetaRequest();

        mockAuthBasic();
        mockUsuario();

        doNothing().when(validaValorMetaValidator).isPositivo(request.getValor());
        doNothing().when(validaFormatoMetaValidator).formatoValido(request.getFormato());
        doNothing().when(validaUsuarioService).porId(1L);

        tested.cadastrar(request);

        verify(validaValorMetaValidator).isPositivo(request.getValor());
        verify(validaFormatoMetaValidator).formatoValido(request.getFormato());
        verify(validaUsuarioService).porId(1L);

        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(1L, true);
        verify(metaRepository).save(metaCaptor.capture());

        Meta meta = metaCaptor.getValue();

        assertEquals(request.getTitulo(), meta.getTitulo());
        assertEquals(request.getValor(), meta.getValorDesejado());
        assertEquals(0, meta.getValorAtual());
        assertTrue(meta.isAtivo());
    }

    @Test
    @DisplayName("Nao deve cadastrar meta com valor <= 0")
    void naoCadastraMetaComValorMenorIgualZero() {

        CadastrarMetaRequest request = cadastrarMetaRequest();
        request.setValor(0.0);

        doThrow(ResponseStatusException.class)
                .when(validaValorMetaValidator)
                .isPositivo(request.getValor());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(validaValorMetaValidator).isPositivo(request.getValor());
        verifyNoInteractions(validaFormatoMetaValidator);
        verifyNoInteractions(usuarioAutenticadoService);
        verify(metaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Nao deve cadastrar meta com formato invalido")
    void naoCadastraMetaComFormatoInvalido() {

        CadastrarMetaRequest request = cadastrarMetaRequest();
        request.setFormato("semestral");

        doNothing().when(validaValorMetaValidator).isPositivo(request.getValor());

        doThrow(ResponseStatusException.class)
                .when(validaFormatoMetaValidator)
                .formatoValido(request.getFormato());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(validaValorMetaValidator).isPositivo(request.getValor());
        verify(validaFormatoMetaValidator).formatoValido(request.getFormato());
        verifyNoInteractions(usuarioAutenticadoService);
        verify(metaRepository, never()).save(any());
    }
}