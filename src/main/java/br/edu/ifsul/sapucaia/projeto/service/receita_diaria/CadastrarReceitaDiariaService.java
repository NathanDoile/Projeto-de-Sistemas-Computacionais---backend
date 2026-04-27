package br.edu.ifsul.sapucaia.projeto.service.receita_diaria;

import br.edu.ifsul.sapucaia.projeto.controller.request.receita_diaria.CadastrarReceitaDiariaRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Meta;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta;
import br.edu.ifsul.sapucaia.projeto.repository.MetaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaDataReceitaDiariaValidator;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaValorReceitaDiariaValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.edu.ifsul.sapucaia.projeto.domain.enums.FormatoMeta.*;
import static br.edu.ifsul.sapucaia.projeto.mapper.AdministradorMapper.toEntity;
import static java.time.DayOfWeek.MONDAY;
import static java.time.LocalDate.now;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Service
@RequiredArgsConstructor
public class CadastrarReceitaDiariaService {

    private final ValidaUsuarioService validaUsuarioService;

    private final UsuarioRepository usuarioRepository;

    private final ReceitaDiariaRepository receitaDiariaRepository;

    private final ValidaValorReceitaDiariaValidator validaValorReceitaDiariaValidator;

    private final ValidaDataReceitaDiariaValidator validaDataReceitaDiariaValidator;

    private final MetaRepository metaRepository;

    @Transactional
    public void cadastrar(CadastrarReceitaDiariaRequest cadastrarReceitaDiariaRequest) {

        validaUsuarioService.porId(cadastrarReceitaDiariaRequest.getIdUsuario());
        validaValorReceitaDiariaValidator.isPositivo(cadastrarReceitaDiariaRequest.getValor());
        validaDataReceitaDiariaValidator.naoMaiorQueHoje(cadastrarReceitaDiariaRequest.getDataReceita());

        ReceitaDiaria receitaDiaria = toEntity(cadastrarReceitaDiariaRequest);

        Usuario usuario = usuarioRepository.findById(cadastrarReceitaDiariaRequest.getIdUsuario()).get();

        List<Meta> metas = usuario.getMetas();

        for(Meta meta : metas){

            LocalDate dataDaReceita = receitaDiaria.getDataReceita();
            LocalDate hoje = now();
            LocalDate inicioSemana = hoje.with(previousOrSame(MONDAY));

            EnumMap<FormatoMeta, Boolean> condicaoParaInsercao = new EnumMap<>(FormatoMeta.class);

            condicaoParaInsercao.put(DIARIA, dataDaReceita.equals(hoje));
            condicaoParaInsercao.put(SEMANAL, dataDaReceita.isAfter(inicioSemana) || dataDaReceita.equals(inicioSemana));
            condicaoParaInsercao.put(MENSAL, dataDaReceita.getMonth() == hoje.getMonth() && dataDaReceita.getYear() == hoje.getYear());

            if(condicaoParaInsercao.get(meta.getFormato())){
                double novoValorMeta = meta.getValorAtual() + receitaDiaria.getValor();

                meta.setValorAtual(novoValorMeta);

                metaRepository.save(meta);
            }
        }

        receitaDiaria.setUsuario(usuario);

        receitaDiaria.setAtivo(true);

        receitaDiariaRepository.save(receitaDiaria);
    }
}
