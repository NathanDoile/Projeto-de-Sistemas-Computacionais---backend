package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.EditarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditarCustoService {

    private final ValidaCustoService validaCustoService;

    private final CustoRepository custoRepository;

    private final ValidaValorCustoValidator validaValorCustoValidator;

    @Transactional
    public void editar(EditarCustoRequest editarCustoRequest) {

        validaCustoService.porId(editarCustoRequest.getIdCusto());
        validaValorCustoValidator.isPositivo(editarCustoRequest.getValor());

        Custo custo = custoRepository.findById(editarCustoRequest.getIdCusto()).get();

        custo.setTipo(editarCustoRequest.getTipo());
        custo.setValor(editarCustoRequest.getValor());
        custo.setDataVencimento(editarCustoRequest.getDataVencimento());
        custo.setDataPagamento(editarCustoRequest.getDataPagamento());
        custo.setDescricao(editarCustoRequest.getDescricao());

        custoRepository.save(custo);
    }
}
