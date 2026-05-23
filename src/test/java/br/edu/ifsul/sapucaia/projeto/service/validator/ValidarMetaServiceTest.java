package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ValidarMetaServiceTest {

    @InjectMocks
    private ValidarMetaService tested;

    @Mock
    private MetaRepository metaRepository;

    @Test
    @DisplayName("Não deve dar erro se meta existe")
    void naoDeveDarErroSeMetaExiste(){

        Long id = 1L;

        when(metaRepository.existsByIdMetaAndIsAtivo(id, true)).thenReturn(true);

        assertDoesNotThrow(() -> tested.porId(id));

        verify(metaRepository).existsByIdMetaAndIsAtivo(id, true);
    }

    @Test
    @DisplayName("Deve dar erro se meta não existe")
    void deveDarErroSeMetaNaoExiste(){

        Long id = 7L;

        when(metaRepository.existsByIdMetaAndIsAtivo(id, true)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> tested.porId(id));

        verify(metaRepository).existsByIdMetaAndIsAtivo(id, true);

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Não existe meta com esse ID", exception.getReason());
    }
}
