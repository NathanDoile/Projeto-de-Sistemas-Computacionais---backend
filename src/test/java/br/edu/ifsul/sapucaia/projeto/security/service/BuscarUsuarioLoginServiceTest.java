package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.controller.response.LoginUsuarioResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.loginUsuarioResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioLoginServiceTest {

    @InjectMocks
    private BuscarUsuarioLoginService tested;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Test
    @DisplayName("Deve retornar o usuario corretamente")
    void deveRetornarUsuarioCorretamente(){

        LoginUsuarioResponse loginUsuarioResponse = loginUsuarioResponse();

        when(usuarioAutenticadoService.getResponse()).thenReturn(loginUsuarioResponse);

        LoginUsuarioResponse response = tested.buscar();

        verify(usuarioAutenticadoService).getResponse();

        assertEquals(loginUsuarioResponse.getIdUsuario(), response.getIdUsuario());
        assertEquals(loginUsuarioResponse.getNome(), response.getNome());
        assertEquals(loginUsuarioResponse.getEmail(), response.getEmail());
        assertEquals(loginUsuarioResponse.getDataCadastro(), response.getDataCadastro());
        assertEquals(loginUsuarioResponse.getTelefone(), response.getTelefone());
        assertEquals(loginUsuarioResponse.isNotificacaoManutencao(), response.isNotificacaoManutencao());
        assertEquals(loginUsuarioResponse.isNotificacaoVencimento(), response.isNotificacaoVencimento());
    }
}
