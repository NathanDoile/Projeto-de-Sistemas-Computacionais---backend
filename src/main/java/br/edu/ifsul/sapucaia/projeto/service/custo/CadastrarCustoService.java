package br.edu.ifsul.sapucaia.projeto.service.custo;

import br.edu.ifsul.sapucaia.projeto.controller.request.custo.CadastrarCustoRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.custo.BuscarCustosEmAbertoResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;
import br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaVeiculoService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaTipoCustoValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorCustoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.*;
import static br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta.CUSTO;
import static br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper.toEntity;
import static br.edu.ifsul.sapucaia.projeto.mapper.CustoMapper.toResponse;
import static java.time.DayOfWeek.MONDAY;
import static java.time.LocalDate.now;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@RequiredArgsConstructor
@Service
public class CadastrarCustoService {

    private final ValidaVeiculoService validaVeiculoService;

    private final VeiculoRepository veiculoRepository;

    private final CustoRepository custoRepository;

    private final ValidaValorCustoValidator validaValorCustoValidator;

    private final ValidaTipoCustoValidator validaTipoCustoValidator;

    private final MetaRepository metaRepository;

    @Transactional
    public BuscarCustosEmAbertoResponse cadastrar(CadastrarCustoRequest request) {

        validaVeiculoService.porId(request.getIdVeiculo());
        validaValorCustoValidator.isPositivo(request.getValor());
        validaTipoCustoValidator.tipoValido(request.getTipo());

        Custo custo = toEntity(request);
      
        Veiculo veiculo = veiculoRepository.findByIdVeiculoAndIsAtivo(request.getIdVeiculo(), true);

        if(request.getDataPagamento() != null){
            salvarMetas(veiculo, custo);
        }

        custo.setVeiculo(veiculo);

        custo.setAtivo(true);

        custoRepository.save(custo);

        return toResponse(custo);
    }

    @Transactional
    private void salvarMetas(Veiculo veiculo, Custo custo){

        List<Meta> metas = veiculo.getUsuario().getMetas()
                .stream()
                .filter(meta -> meta.getTipo().equals(CUSTO))
                .toList();

        for(Meta meta : metas){

            LocalDate dataPagamento = custo.getDataPagamento();
            LocalDate hoje = now();
            LocalDate inicioSemana = hoje.with(previousOrSame(MONDAY));

            EnumMap<FormatoMeta, Boolean> condicaoParaInsercao = new EnumMap<>(FormatoMeta.class);

            condicaoParaInsercao.put(DIARIA, dataPagamento.equals(hoje));
            condicaoParaInsercao.put(SEMANAL, dataPagamento.isAfter(inicioSemana) || dataPagamento.equals(inicioSemana));
            condicaoParaInsercao.put(MENSAL, dataPagamento.getMonth() == hoje.getMonth() && dataPagamento.getYear() == hoje.getYear());

            if(Boolean.TRUE.equals(condicaoParaInsercao.get(meta.getFormato()))){
                double novoValorMeta = meta.getValorAtual() + custo.getValor();

                meta.setValorAtual(novoValorMeta);

                metaRepository.save(meta);
            }
        }
    }
}
