package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.controller.response.manutencao.ManutencaoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
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

import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.factory.UsuarioFactory.usuarioSecurity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarManutencaoServiceTest {

    @InjectMocks
    private ListarManutencaoService tested;

    @Mock
    private ManutencaoRepository manutencaoRepository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Test
    @DisplayName("Deve listar as manutenções do usuário paginadas corretamente")
    void deveListarManutencoesCorretamente() {

        Pageable pageable = PageRequest.of(0, 10);

        UsuarioSecurity usuarioSecurity = usuarioSecurity();

        Manutencao manutencao = Manutencao.builder()
                .idManutencao(1L)
                .descricao("Troca de óleo")
                .isAtivo(true)
                .build();

        Page<Manutencao> page = new PageImpl<>(List.of(manutencao));

        when(usuarioAutenticadoService.getUser()).thenReturn(usuarioSecurity);
        when(manutencaoRepository
                .findAllByVeiculoUsuarioIdUsuarioAndIsAtivo(
                        usuarioSecurity.getId(),
                        true,
                        pageable))
                .thenReturn(page);

        Page<ManutencaoResponse> response = tested.listar(pageable);

        verify(usuarioAutenticadoService).getUser();

        verify(manutencaoRepository)
                .findAllByVeiculoUsuarioIdUsuarioAndIsAtivo(
                        usuarioSecurity.getId(),
                        true,
                        pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(
                manutencao.getIdManutencao(),
                response.getContent().get(0).idManutencao()
        );
        assertEquals(
                manutencao.getDescricao(),
                response.getContent().get(0).descricao()
        );
    }
}