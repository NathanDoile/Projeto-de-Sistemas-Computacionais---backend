package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaTelefoneUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.editarPerfilUsuarioRequest;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditarPerfilUsuarioServiceTest {

    @InjectMocks
    private EditarPerfilUsuarioService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Mock
    private ValidaEmailUsuarioService validaEmailUsuarioService;

    @Mock
    private ValidaTelefoneUsuarioService validaTelefoneUsuarioService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve editar perfil com os dados corretos")
    void deveEditarPerfilComDadosCorretos(){

        EditarPerfilUsuarioRequest request = editarPerfilUsuarioRequest();
        Long id = 1L;

        Usuario usuario = usuario();

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)).thenReturn(Optional.of(usuario));

        tested.editarPerfilUsuario(id, request);

        verify(validaUsuarioService).porId(id);
        verify(validaEmailUsuarioService).validaEmailUnicoParaEdicao(request.getEmail(), id);
        verify(validaTelefoneUsuarioService).validaTelefoneUnicoParaEdicao(request.getTelefone(), id);
        verify(usuarioRepository).findByIdUsuarioAndIsAtivo(id, true);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario response = usuarioCaptor.getValue();

        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getNome(), response.getNome());
        assertEquals(request.getTelefone(), response.getTelefone());
    }

    //EDITAR APENAS COM TELEFONE
    //EDITAR APENAS COM EMAIL
    //EDITAR APENAS COM NOME
    //ERRO NO ID
    //ERRO NO EMAIL
    //ERRO NO TELEFONE
}
