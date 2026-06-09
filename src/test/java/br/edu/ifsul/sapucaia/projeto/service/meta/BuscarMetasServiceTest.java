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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.DIARIA;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.MENSAL;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.CUSTO;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.RECEITA;
import static br.edu.ifsul.sapucaia.projeto.factory.MetaFactory.meta;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        Pageable pageable = PageRequest.of(0, 10);

        List<Meta> metas = List.of(meta(DIARIA, RECEITA), meta(MENSAL, CUSTO));
        Page<Meta> pageMetas = new PageImpl<>(metas, pageable, metas.size());

        when(metaRepository.findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true, pageable)).thenReturn(pageMetas);

        Page<BuscarMetaResponse> response = tested.buscar(idUsuario, pageable);

        verify(validaUsuarioService).porId(idUsuario);
        verify(metaRepository).findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true, pageable);

        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getContent().size());

        for(int i = 0; i < response.getContent().size(); i++){
            assertEquals(metas.get(i).getIdMeta(), response.getContent().get(i).getIdMeta());
            assertEquals(metas.get(i).getTitulo(), response.getContent().get(i).getTitulo());
            assertEquals(metas.get(i).getFormato(), response.getContent().get(i).getFormato());
            assertEquals(metas.get(i).getValorDesejado(), response.getContent().get(i).getValorDesejado());
            assertEquals(metas.get(i).getValorAtual(), response.getContent().get(i).getValorAtual());
        }
    }

    @Test
    @DisplayName("Não deve buscar as metas com id do usuario incorreto")
    void naoDeveBuscarMetasIdUsuarioIncorreto(){

        Long idUsuario = 1L;
        Pageable pageable = PageRequest.of(0, 10); 

        doThrow(ResponseStatusException.class).when(validaUsuarioService).porId(idUsuario);

        assertThrows(ResponseStatusException.class, () -> tested.buscar(idUsuario, pageable));

        verify(validaUsuarioService).porId(idUsuario);
        verify(metaRepository, never()).findByUsuarioIdUsuarioAndIsAtivo(any(Long.class), any(Boolean.class), any(Pageable.class));
    }
}