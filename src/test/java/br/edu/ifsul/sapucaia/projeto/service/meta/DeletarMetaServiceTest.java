package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
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

import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.meta;
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

    @Captor
    private ArgumentCaptor<Meta> metaCaptor;

    @Test
    @DisplayName("Deve excluir a conta corretamente")
    void deveExcluirContaCorretamente(){

        Long id = 1L;

        Meta meta = meta();

        when(metaRepository.findByIdMetaAndIsAtivo(id, true)).thenReturn(meta);

        tested.deletar(id);

        verify(validarMetaService).porId(id);
        verify(metaRepository).findByIdMetaAndIsAtivo(id, true);
        verify(metaRepository).save(metaCaptor.capture());

        Meta response = metaCaptor.getValue();

        assertEquals(id, response.getIdMeta());
        assertFalse(response.isAtivo());
    }

    @Test
    @DisplayName("Nao deve excluir a conta se id incorreto")
    void naoDeveExcluirContaSeIdIncorreto(){

        Long id = 7L;

        doThrow(ResponseStatusException.class).when(validarMetaService).porId(id);

        assertThrows(ResponseStatusException.class, () -> tested.deletar(id));

        verify(validarMetaService).porId(id);
        verify(metaRepository, never()).findByIdMetaAndIsAtivo(any(Long.class), any(Boolean.class));
        verify(metaRepository, never()).save(any(Meta.class));
    }
}
