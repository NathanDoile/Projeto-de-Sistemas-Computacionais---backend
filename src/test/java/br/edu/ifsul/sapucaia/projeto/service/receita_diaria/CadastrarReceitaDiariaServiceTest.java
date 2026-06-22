package br.edu.ifsul.sapucaia.projeto.service.receita_diaria;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarReceitaDiariaServiceTest {

    @InjectMocks
    private CadastrarReceitaDiariaService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReceitaDiariaRepository receitaDiariaRepository;

    @Mock
    private ValidaValorReceitaDiariaValidator validaValorReceitaDiariaValidator;

    @Mock
    private ValidaDataReceitaDiariaValidator validaDataReceitaDiariaValidator;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private MetaRepository metaRepository;

    @Test
    void deveCadastrarReceitaDiariaCorretamente() {

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();
        Usuario usuario = usuario();
        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);

        when(usuarioRepository.findByIdUsuarioAndIsAtivo(
                usuarioSecurity.getId(), true))
                .thenReturn(Optional.of(usuario));

        tested.cadastrar(request);

        verify(validaValorReceitaDiariaValidator)
                .isPositivo(request.getValor());

        verify(validaDataReceitaDiariaValidator)
                .naoMaiorQueHoje(request.getDataReceita());

        verify(usuarioRepository)
                .findByIdUsuarioAndIsAtivo(usuarioSecurity.getId(), true);

        verify(receitaDiariaRepository).save(any());
    }

    @Test
    void naoDeveCadastrarSeValorInvalido() {

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        doThrow(ResponseStatusException.class)
                .when(validaValorReceitaDiariaValidator)
                .isPositivo(request.getValor());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(usuarioAutenticadoService, never()).getUser();
        verify(receitaDiariaRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarSeDataInvalida() {

        CadastrarReceitaDiariaRequest request = cadastrarReceitaDiariaRequest();

        doThrow(ResponseStatusException.class)
                .when(validaDataReceitaDiariaValidator)
                .naoMaiorQueHoje(request.getDataReceita());

        assertThrows(ResponseStatusException.class,
                () -> tested.cadastrar(request));

        verify(usuarioAutenticadoService, never()).getUser();
        verify(receitaDiariaRepository, never()).save(any());
    }
}