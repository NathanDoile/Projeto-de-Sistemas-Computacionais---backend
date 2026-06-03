package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.LoginUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.LoginUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.LoginUsuarioRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class LoginUsuarioServiceTest {

    @InjectMocks
    private LoginUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Test
    @DisplayName("Deve realizar o login corretamente")
    void deveRealizarLoginCorretamente() {

        LoginUsuarioRequest request = LoginUsuarioRequest();

        Usuario usuario = usuario();

        doNothing().when(validaUsuarioService)
                .porEmail(request.getEmail());

        when(usuarioRepository.findByEmailAndIsAtivo(
                request.getEmail(),
                true))
                .thenReturn(Optional.of(usuario));

        LoginUsuarioResponse response = tested.loginUsuario(request);

        verify(validaUsuarioService)
                .porEmail(request.getEmail());

        verify(usuarioRepository)
                .findByEmailAndIsAtivo(request.getEmail(), true);

        verify(usuarioRepository, never())
                .delete(any(Usuario.class));

        verify(validaSenhaAtualUsuarioService)
                .validaSenhaAtualUsuario(request.getSenha(), usuario.getIdUsuario());

        assertEquals(usuario.getIdUsuario(), response.getIdUsuario());
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(usuario.getDataCadastro(), response.getDataCadastro());
        assertEquals(usuario.getTelefone(), response.getTelefone());
        assertEquals(usuario.isNotificacaoVencimento(), response.isNotificacaoVencimento());
        assertEquals(usuario.isNotificacaoManutencao(), response.isNotificacaoManutencao());
    }

    @Test
    @DisplayName("Não deve realizar o login quando e-mail estiver incorreto")
    void naoDeveRealizarLoginComEmailInvalido() {

        LoginUsuarioRequest request = LoginUsuarioRequest();

        doThrow(new ResponseStatusException(
                NOT_FOUND,
                "E-mail ou senha inválido"))
                .when(validaUsuarioService)
                .porEmail(request.getEmail());

        assertThrows(
                ResponseStatusException.class,
                () -> tested.loginUsuario(request)
        );

        verify(validaUsuarioService)
                .porEmail(request.getEmail());

        verify(usuarioRepository, never())
                .findByEmailAndIsAtivo(anyString(), anyBoolean());

        verify(usuarioRepository, never())
                .delete(any(Usuario.class));

        verify(validaSenhaAtualUsuarioService, never())
                .validaSenhaAtualUsuario(anyString(), anyLong());
    }

    @Test
    @DisplayName("Não deve realizar o login quando usuario nao tiver veiculo")
    void naoDeveRealizarLoginQuandoUsuarioNaoTiverVeiculo() {

        LoginUsuarioRequest request = LoginUsuarioRequest();

        Usuario usuario = usuario();
        usuario.setPossuiVeiculo(false);

        doNothing().when(validaUsuarioService)
                .porEmail(request.getEmail());

        when(usuarioRepository.findByEmailAndIsAtivo(
                request.getEmail(),
                true))
                .thenReturn(Optional.of(usuario));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> tested.loginUsuario(request)
        );

        verify(validaUsuarioService)
                .porEmail(request.getEmail());

        verify(usuarioRepository)
                .findByEmailAndIsAtivo(request.getEmail(), true);

        verify(usuarioRepository)
                .delete(usuario);

        verify(validaSenhaAtualUsuarioService, never())
                .validaSenhaAtualUsuario(anyString(), anyLong());

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("E-mail ou senha inválido", exception.getReason());
    }

    @Test
    @DisplayName("Não deve realizar o login quando senha estiver incorreto")
    void naoDeveRealizarLoginComSenhaInvalida() {

        LoginUsuarioRequest request = LoginUsuarioRequest();

        Usuario usuario = usuario();

        doNothing().when(validaUsuarioService)
                .porEmail(request.getEmail());

        when(usuarioRepository.findByEmailAndIsAtivo(
                request.getEmail(),
                true))
                .thenReturn(Optional.of(usuario));

        doThrow(ResponseStatusException.class)
                .when(validaSenhaAtualUsuarioService)
                .validaSenhaAtualUsuario(
                        request.getSenha(),
                        usuario.getIdUsuario()
                );

        assertThrows(
                ResponseStatusException.class,
                () -> tested.loginUsuario(request)
        );

        verify(validaUsuarioService)
                .porEmail(request.getEmail());

        verify(usuarioRepository)
                .findByEmailAndIsAtivo(request.getEmail(), true);

        verify(usuarioRepository, never())
                .delete(any(Usuario.class));

        verify(validaSenhaAtualUsuarioService)
                .validaSenhaAtualUsuario(
                        request.getSenha(),
                        usuario.getIdUsuario()
                );
    }
}