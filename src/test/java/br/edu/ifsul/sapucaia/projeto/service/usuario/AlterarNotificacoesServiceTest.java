package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarNotificacoesRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private ValidaTipoNotificacaoValidator validaTipoNotificacaoValidator;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Manutenção e estiver ativada")
    void deveAlterarEstadoNotificacaoManutencaoAtivada(){

        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Usuario usuario = usuario();

        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(usuario.getIdUsuario(), response.getIdUsuario());
        assertFalse(response.isNotificacaoManutencao());
    }

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Manutenção e estiver desativada")
    void deveAlterarEstadoNotificacaoManutencaoDesativada(){

        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Usuario usuario = usuario();
        usuario.setNotificacaoManutencao(false);

        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(usuario.getIdUsuario(), response.getIdUsuario());
        assertTrue(response.isNotificacaoManutencao());
    }

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Vencimento e estiver ativada")
    void deveAlterarEstadoNotificacaoVencimentoAtivada(){

        AlterarNotificacoesRequest request = alterarNotificacaoVencimentoRequest();

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Usuario usuario = usuario();

        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(usuario.getIdUsuario(), response.getIdUsuario());
        assertFalse(response.isNotificacaoVencimento());
    }

    @Test
    @DisplayName("Deve alterar estado da notificação se for notificação de Vencimento e estiver desativada")
    void deveAlterarEstadoNotificacaoVencimentoDesativada(){

        AlterarNotificacoesRequest request = alterarNotificacaoVencimentoRequest();

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Usuario usuario = usuario();
        usuario.setNotificacaoVencimento(false);

        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.of(usuario));

        tested.alterarNotificacoes(request);

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(usuario.getIdUsuario(), response.getIdUsuario());
        assertTrue(response.isNotificacaoVencimento());
    }

    @Test
    @DisplayName("Não deve alterar estado da notificação se usuário não encontrado")
    void naoDeveAlterarEstadoNotificacaoUsuarioInvalido(){

        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        Long id = usuarioLogado.getId();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        when(usuarioRepository.findByIdUsuario(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tested.alterarNotificacoes(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository).findByIdUsuario(id);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve alterar estado da notificação se tipo de notificação for invalido")
    void naoDeveAlterarEstadoNotificacaoInvalida(){

        AlterarNotificacoesRequest request = alterarNotificacaoManutencaoRequest();
        request.setNotificacao("Errada");

        UsuarioSecurity usuarioLogado = usuarioSecurity();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioLogado);
        doThrow(ResponseStatusException.class).when(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());

        assertThrows(ResponseStatusException.class, () -> tested.alterarNotificacoes(request));

        verify(usuarioAutenticadoService).getUser();
        verify(validaTipoNotificacaoValidator).tipoValido(request.getNotificacao());
        verify(usuarioRepository, never()).findByIdUsuario(anyLong());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}