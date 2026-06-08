package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarManutencaoServiceTest {

    @InjectMocks
    private ListarManutencaoService tested;

    @Mock
    private ManutencaoRepository manutencaoRepository;

    @Mock
    private ValidaUsuarioService validaUsuarioService;

    @Test
    @DisplayName("Deve listar as manutenções do usuário paginadas corretamente")
    void deveListarManutencoesCorretamente() {

        Long idUsuario = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Manutencao manutencao = Manutencao.builder()
                .idManutencao(1L)
                .descricao("Troca de óleo")
                .isAtivo(true)
                .build();

        Page<Manutencao> page = new PageImpl<>(List.of(manutencao));

        when(manutencaoRepository
                .findAllByVeiculoUsuarioIdUsuarioAndIsAtivo(
                        idUsuario,
                        true,
                        pageable))
                .thenReturn(page);

        Page<Manutencao> response = tested.listar(idUsuario, pageable);

        verify(validaUsuarioService).porId(idUsuario);

        verify(manutencaoRepository)
                .findAllByVeiculoUsuarioIdUsuarioAndIsAtivo(
                        idUsuario,
                        true,
                        pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(
                manutencao.getIdManutencao(),
                response.getContent().get(0).getIdManutencao()
        );
        assertEquals(
                manutencao.getDescricao(),
                response.getContent().get(0).getDescricao()
        );
    }
}