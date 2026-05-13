package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.EditarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.factory.CustoFactory;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoCustoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.factory.CustoFactory.editarCustoRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarCustoServiceTest {

    @InjectMocks
    private EditarCustoService tested;

    @Mock
    private ValidaCustoService validaCustoService;

    @Mock
    private CustoRepository custoRepository;

    @Mock
    private ValidaValorCustoValidator validaValorCustoValidator;

    @Mock
    private ValidaTipoCustoValidator validaTipoCustoValidator;

    @Captor
    private ArgumentCaptor<Custo> custoCaptor;

    @Test
    @DisplayName("Deve editar custo corretamente")
    void deveEditarCustoCorretamente() {

        EditarCustoRequest request = editarCustoRequest();
        Long id = 1L;
        Custo custo = CustoFactory.custo();

        when(custoRepository.findById(id)).thenReturn(Optional.of(custo));

        tested.editar(request);

        verify(validaCustoService).porId(id);
        verify(validaValorCustoValidator).isPositivo(request.getValor());
        verify(validaTipoCustoValidator).tipoValido(request.getTipo());
        verify(custoRepository).findById(id);
        verify(custoRepository).save(custoCaptor.capture());

        Custo response = custoCaptor.getValue();

        assertEquals(TipoCusto.COMBUSTIVEL, response.getTipo());
        assertEquals(request.getValor(), response.getValor());
        assertEquals(request.getDataVencimento(), response.getDataVencimento());
        assertEquals(request.getDataPagamento(), response.getDataPagamento());
        assertEquals(request.getDescricao(), response.getDescricao());
    }

    @Test
    @DisplayName("Não deve editar custo se custo for inválido")
    void naoDeveEditarCustoSeCustoForInvalido() {

        Long idCusto = 1L;
        EditarCustoRequest request = editarCustoRequest();

        doThrow(ResponseStatusException.class).when(validaCustoService).porId(idCusto);

        assertThrows(ResponseStatusException.class, () -> tested.editar(request));

        verify(validaCustoService).porId(idCusto);
        verify(validaValorCustoValidator, never()).isPositivo(anyDouble());
        verify(validaTipoCustoValidator, never()).tipoValido(anyString());
        verify(custoRepository, never()).findById(anyLong());
        verify(custoRepository, never()).save(any(Custo.class));
    }
}
