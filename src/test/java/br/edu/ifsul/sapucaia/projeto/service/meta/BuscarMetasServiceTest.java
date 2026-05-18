package br.edu.ifsul.sapucaia.projeto.service.meta;

import br.edu.ifsul.sapucaia.projeto.controller.response.meta.BuscarMetaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.meta;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarMetasServiceTest {

    @InjectMocks
    private BuscarMetasService tested;

    @Mock
    private ValidaUsuarioService validaUsuarioService;


    @Mock
    private MetaRepository metaRepository;

    @Test
    @DisplayName("Deve buscar as metas corretamente")
    void deveBuscarMetasCorretamente(){

        Long idUsuario = 1L;

        List<Meta> metas = List.of(meta(), meta());

        when(metaRepository.findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true)).thenReturn(metas);

        List<BuscarMetaResponse> response = tested.buscar(idUsuario);

        verify(validaUsuarioService).porId(idUsuario);
        verify(metaRepository).findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true);

        assertEquals(2, response.size());

        for(int i = 0; i < response.size(); i++){
            assertEquals(metas.get(i).getIdMeta(), response.get(i).getIdMeta());
            assertEquals(metas.get(i).getTitulo(), response.get(i).getTitulo());
            assertEquals(metas.get(i).getFormato(), response.get(i).getFormato());
            assertEquals(metas.get(i).getValorDesejado(), response.get(i).getValorDesejado());
            assertEquals(metas.get(i).getValorAtual(), response.get(i).getValorAtual());
        }
    }

    @Test
    @DisplayName("Não deve buscar as metas com id do usuario incorreto")
    void naoDeveBuscarMetasIdUsuarioIncorreto(){

        Long idUsuario = 1L;

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(idUsuario);

        assertThrows(ResponseStatusException.class, () -> tested.buscar(idUsuario));

        verify(validaUsuarioService).porId(idUsuario);
        verify(metaRepository, never()).findByUsuarioIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class));
    }
}
