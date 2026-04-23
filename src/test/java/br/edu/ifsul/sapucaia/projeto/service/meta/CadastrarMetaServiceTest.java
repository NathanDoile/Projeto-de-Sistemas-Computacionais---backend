package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.request.meta.CadastrarMetaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static br.edu.ifsul.sapucaia.projeto.repository.MetaRepository.cadastrarMetaRequest;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CadastrarMetaServiceTest {

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

    @Captor
        private ArgumentCaptor<Meta> metaCaptor;

    @Test
    @DisplayName("Deve cadastrar meta")
    void cadastraMetaComValoresCorretos(){

        CadastrarMetaRequest request = cadastrarMetaRequest();

        Usuario usuario = usuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(request.getIdUsuario(), true)).thenReturn(of(usuario));

        tested.cadastrar(request);

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorMetaValidator).isPositivo(request.getValor());
        verify(validaFormatoMetaValidator).formatoValido(request.getFormato());
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(request.getIdUsuario(), true);
        verify(metaRepository).save(metaCaptor.capture());

        Meta metaResponse = metaCaptor.getValue();

        assertEquals(request.getTitulo(), metaResponse.getTitulo());
        assertEquals(request.getValor(), metaResponse.getValorDesejado());
        assertEquals(request.getFormato(), metaResponse.getFormato().toString());
        assertEquals(usuario, metaResponse.getUsuario());
        assertTrue(metaResponse.isAtivo());
        assertEquals(0, metaResponse.getValorAtual());
    }

    @Test
    @DisplayName("Nao deve cadastrar meta com id usuario errado")
    void naoCadastraMetaComIdUsuarioErrado(){
        CadastrarMetaRequest request = cadastrarMetaRequest();

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(request.getIdUsuario());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrar(request));

        verify(validaUsuarioService).porId(request.getIdUsuario());
        verify(validaValorMetaValidator,never()).isPositivo(any(Double.class));
        verify(validaFormatoMetaValidator,never()).formatoValido(any(String.class));
        verify(usuarioRepository,never()).findByIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(metaRepository,never()).save(any(Meta.class));
    }
}
