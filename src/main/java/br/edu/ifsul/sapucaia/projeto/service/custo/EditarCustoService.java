package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.EditarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaCustoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoCustoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.*;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto.deTexto;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.CUSTO;
import static java.time.DayOfWeek.MONDAY;
import static java.time.LocalDate.now;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Service
@RequiredArgsConstructor
public class EditarCustoService {

    private final ValidaCustoService validaCustoService;

    private final CustoRepository custoRepository;

    private final ValidaValorCustoValidator validaValorCustoValidator;

    private final ValidaTipoCustoValidator validaTipoCustoValidator;

    private final MetaRepository metaRepository;

    @Transactional
    public void editar(EditarCustoRequest editarCustoRequest) {

        validaCustoService.porId(editarCustoRequest.getIdCusto());
        validaValorCustoValidator.isPositivo(editarCustoRequest.getValor());
        validaTipoCustoValidator.tipoValido(editarCustoRequest.getTipo());

        Custo custo = custoRepository.findByIdCustoAndIsAtivo(
                        editarCustoRequest.getIdCusto(), true)
                .get();

        if(editarCustoRequest.getValor() != custo.getValor()){
            adicionarNovoValorNasMetas(custo, editarCustoRequest.getValor(), editarCustoRequest.getDataPagamento());
        }

        custo.setTipo(deTexto(editarCustoRequest.getTipo()));
        custo.setValor(editarCustoRequest.getValor());
        custo.setDataVencimento(editarCustoRequest.getDataVencimento());
        custo.setDataPagamento(editarCustoRequest.getDataPagamento());
        custo.setDescricao(editarCustoRequest.getDescricao());

        custoRepository.save(custo);
    }

    @Transactional
    private void adicionarNovoValorNasMetas(Custo custo, double novoValor, LocalDate novaData){

        List<Meta> metas = custo.getVeiculo().getUsuario().getMetas()
                .stream()
                .filter(meta -> meta.getTipo().equals(CUSTO))
                .toList();

        for(Meta meta : metas){
            LocalDate dataPagamento = custo.getDataPagamento();
            LocalDate hoje = now();
            LocalDate inicioSemana = hoje.with(previousOrSame(MONDAY));

            EnumMap<FormatoMeta, Boolean> condicaoParaRemocao = new EnumMap<>(FormatoMeta.class);

            condicaoParaRemocao.put(DIARIA, dataPagamento.equals(hoje));
            condicaoParaRemocao.put(SEMANAL, dataPagamento.isAfter(inicioSemana) || dataPagamento.equals(inicioSemana));
            condicaoParaRemocao.put(MENSAL, dataPagamento.getMonth() == hoje.getMonth() && dataPagamento.getYear() == hoje.getYear());

            double novoValorMeta = meta.getValorAtual();

            if(Boolean.TRUE.equals(condicaoParaRemocao.get(meta.getFormato()))){
                novoValorMeta = meta.getValorAtual() - custo.getValor();
            }

            EnumMap<FormatoMeta, Boolean> condicaoParaInsercao = new EnumMap<>(FormatoMeta.class);

            condicaoParaInsercao.put(DIARIA, novaData.equals(hoje));
            condicaoParaInsercao.put(SEMANAL, novaData.isAfter(inicioSemana) || novaData.equals(inicioSemana));
            condicaoParaInsercao.put(MENSAL, novaData.getMonth() == hoje.getMonth() && novaData.getYear() == hoje.getYear());

            if(Boolean.TRUE.equals(condicaoParaInsercao.get(meta.getFormato()))){
                novoValorMeta = novoValorMeta + novoValor;
            }

            meta.setValorAtual(novoValorMeta);

            metaRepository.save(meta);
        }
    }
}
