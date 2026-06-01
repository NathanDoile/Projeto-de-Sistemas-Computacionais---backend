package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarNotificacoesRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoNotificacaoValidator;
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

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarNotificacoesServiceTest {

    @InjectMocks
    private AlterarNotificacoesService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private ValidaTipoNotificacaoValidator validaTipoNotificacaoValidator;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Manutenção e estiver ativada")
    void deveAlterarEstadoNotificacaoManutencaoAtivada(){
        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();

        Usuario usuario = usuario();
        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(id, request);

        verify(validaUsuarioService).porId(id);
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(id, response.getIdUsuario());
        assertFalse(response.isNotificacaoManutencao());
    }

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Manutenção e estiver desativada")
    void deveAlterarEstadoNotificacaoManutencaoDesativada(){
        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();

        Usuario usuario = usuario();
        usuario.setNotificacaoManutencao(false);
        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(id, request);

        verify(validaUsuarioService).porId(id);
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(id, response.getIdUsuario());
        assertTrue(response.isNotificacaoManutencao());
    }

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Vencimento e estiver ativada")
    void deveAlterarEstadoNotificacaoVencimentoAtivada(){
        AlterarNotificacoesRequest request = alterarNotificacaoVencimentoRequest();

        Usuario usuario = usuario();
        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(id, request);

        verify(validaUsuarioService).porId(id);
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(id, response.getIdUsuario());
        assertFalse(response.isNotificacaoVencimento());
    }

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Vencimento e estiver desativada")
    void deveAlterarEstadoNotificacaoVencimentoDesativada(){
        AlterarNotificacoesRequest request = alterarNotificacaoVencimentoRequest();

        Usuario usuario = usuario();
        usuario.setNotificacaoVencimento(false);
        Long id = usuario.getIdUsuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(id, request);

        verify(validaUsuarioService).porId(id);
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(id, response.getIdUsuario());
        assertTrue(response.isNotificacaoVencimento());
    }

    @Test
    @DisplayName("Não deve alterar estado da notificação se usuário não encontrado")
    void naoDeveAlterarEstadoNotificacaoUsuarioInvalido(){
        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();

        Usuario usuario = usuario();
        Long id = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.alterarNotificacoes(id, request));

        verify(validaUsuarioService).porId(id);
        verify(validaTipoNotificacaoValidator, never()).tipoValido(request.getNotificacao());
        verify(usuarioRepository, never()).findById(id);
        verify(usuarioRepository, never()).save(usuarioCaptor.capture());
    }

    @Test
    @DisplayName("Não deve alterar estado da notificação se tipo de notificação for invalido")
    void naoDeveAlterarEstadoNotificacaoInvalida(){
        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();
        request.setNotificacao("Errada");

        Usuario usuario = usuario();
        Long id = usuario.getIdUsuario();

        doThrow(ResponseStatusException.class).when(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());

        assertThrows(ResponseStatusException.class, () -> tested.alterarNotificacoes(id, request));

        verify(validaUsuarioService).porId(id);
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository, never()).findById(id);
        verify(usuarioRepository, never()).save(usuarioCaptor.capture());
    }
}
