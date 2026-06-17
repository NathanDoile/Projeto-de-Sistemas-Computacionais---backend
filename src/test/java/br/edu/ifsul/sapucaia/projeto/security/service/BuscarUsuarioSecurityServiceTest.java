package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioSecurityServiceTest {

    @InjectMocks
    private BuscarUsuarioSecurityService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar o usuario corretamente")
    void deveRetornarUsuarioCorretamente(){

        String email = "teste@gmail.com";

        Usuario usuario = usuario();

        when(usuarioRepository.findByEmailAndIsAtivo(email, true)).thenReturn(Optional.of(usuario));

        UsuarioSecurity response = (UsuarioSecurity) tested.loadUserByUsername(email);

        verify(usuarioRepository).findByEmailAndIsAtivo(email, true);
        verify(usuarioRepository, never()).delete(any(Usuario.class));

        assertEquals(usuario.getIdUsuario(), response.getId());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(usuario.getSenha(), response.getPassword());
        assertEquals(usuario.getVeiculo().getIdVeiculo(), response.getIdVeiculo());
        assertTrue(response.isAtivo());
    }

    @Test
    @DisplayName("Não deve retornar usuario se usuario não existir")
    void naoDeveRetornarUsuarioSeNaoExistir(){

        String email = "anonymousUser";

        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> tested.loadUserByUsername(email));

        verify(usuarioRepository).findByEmailAndIsAtivo(email, true);
        verify(usuarioRepository, never()).delete(any(Usuario.class));

        assertEquals("Credenciais inválidas ou usuário inativo.", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve retornar usuario se usuario não tiver veiculo")
    void naoDeveRetornarUsuarioSeNaoTiverVeiculo(){

        String email = "teste@gmail.com";

        Usuario usuario = usuario();
        usuario.setPossuiVeiculo(false);

        when(usuarioRepository.findByEmailAndIsAtivo(email, true)).thenReturn(Optional.of(usuario));

        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> tested.loadUserByUsername(email));

        verify(usuarioRepository).findByEmailAndIsAtivo(email, true);
        verify(usuarioRepository).delete(usuario);

        assertEquals("Credenciais inválidas ou usuário inativo.", exception.getMessage());
    }
}
