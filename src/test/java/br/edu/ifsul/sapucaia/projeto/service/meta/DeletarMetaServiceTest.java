package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidarMetaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.DIARIA;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.RECEITA;
import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.meta;
import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarMetaServiceTest {

    @InjectMocks
    private DeletarMetaService tested;

    @Mock
    private MetaRepository metaRepository;

    @Mock
    private ValidarMetaService validarMetaService;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Captor
    private ArgumentCaptor<Meta> metaCaptor;

    @Test
    @DisplayName("Deve excluir a conta corretamente")
    void deveExcluirContaCorretamente(){

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Long id = 1L;

        Meta meta = meta(DIARIA, RECEITA);

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(metaRepository.findByUsuarioIdUsuarioAndUsuarioIsAtivoAndIdMetaAndIsAtivo(usuarioSecurity.getId(), true, id, true)).thenReturn(meta);

        tested.deletar(id);

        verify(usuarioAutenticadoService).getUser();
        verify(validarMetaService).porId(id);
        verify(metaRepository).findByUsuarioIdUsuarioAndUsuarioIsAtivoAndIdMetaAndIsAtivo(usuarioSecurity.getId(), true, id, true);
        verify(metaRepository).save(metaCaptor.capture());

        Meta response = metaCaptor.getValue();

        assertEquals(id, response.getIdMeta());
        assertFalse(response.isAtivo());
    }

    @Test
    @DisplayName("Nao deve excluir a conta se id incorreto")
    void naoDeveExcluirContaSeIdIncorreto(){

        Long id = 7L;

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity());
        doThrow(ResponseStatusException.class).when(validarMetaService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.deletar(id));

        verify(validarMetaService).porId(id);
        verify(metaRepository, never()).findByUsuarioIdUsuarioAndUsuarioIsAtivoAndIdMetaAndIsAtivo(any(Long.class), any(Boolean.class), any(Long.class), any(Boolean.class));
        verify(metaRepository, never()).save(any(Meta.class));
    }
}
