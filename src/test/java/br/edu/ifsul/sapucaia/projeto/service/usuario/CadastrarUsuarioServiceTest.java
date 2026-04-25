package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaTelefoneUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.cadastrarUsuarioRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CadastrarUsuarioServiceTest {

    @InjectMocks
    CadastrarUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaEmailUsuarioService validaEmailUsuarioService;

    @Mock
    private ValidaTelefoneUsuarioService validaTelefoneUsuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve cadastrar usuario")
    void CadastraUsuarioComValoresCorretos(){
        CadastrarUsuarioRequest request = cadastrarUsuarioRequest();

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        tested.cadastrarUsuario(request);

        verify(validaEmailUsuarioService).validaEmailUnico(request.getEmail());
        verify(validaTelefoneUsuarioService).validaTelefoneUnico(request.getTelefone());
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario usuarioResponse = usuarioCaptor.getValue();

        assertEquals(request.getNome(), usuarioResponse.getNome());
        assertEquals(request.getEmail(), usuarioResponse.getEmail());
        assertEquals(request.getSenha(), usuarioResponse.getSenha());
        assertEquals(request.getTelefone(), usuarioResponse.getTelefone());
        assertTrue(usuarioResponse.isAtivo());
        assertFalse(usuarioResponse.isPossuiVeiculo());
        assertFalse(usuarioResponse.isNotificacaoManutencao());
        assertFalse(usuarioResponse.isNotificacaoVencimento());
    }

    @Test
    @DisplayName("Nao deve cadastrar usuario, email ja existe")
    void naoCadastraUsuarioEmailInvalido(){
        CadastrarUsuarioRequest request = cadastrarUsuarioRequest();

        request.setEmail("emailcadastroteste@gmail.com");

        doThrow(ResponseStatusException.class).when(validaEmailUsuarioService).validaEmailUnico(request.getEmail());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrarUsuario(request));

        verify(validaEmailUsuarioService).validaEmailUnico(request.getEmail());
        verify(validaTelefoneUsuarioService, never()).validaTelefoneUnico(request.getTelefone());
        verify(usuarioRepository, never()).save(usuarioCaptor.capture());
    }

    @Test
    @DisplayName("Nao deve cadastrar usuario, telefone ja existe")
    void naoCadastraUsuarioTelefoneInvalido(){
        CadastrarUsuarioRequest request = cadastrarUsuarioRequest();

        request.setTelefone("51888888888");

        doThrow(ResponseStatusException.class).when(validaTelefoneUsuarioService).validaTelefoneUnico(request.getTelefone());

        assertThrows(ResponseStatusException.class, () -> tested.cadastrarUsuario(request));

        verify(validaEmailUsuarioService).validaEmailUnico(request.getEmail());
        verify(validaTelefoneUsuarioService).validaTelefoneUnico(request.getTelefone());
        verify(usuarioRepository, never()).save(usuarioCaptor.capture());
    }
}
