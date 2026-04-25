package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.*;
import br.edu.ifsul.sapucaia.projeto.service.validator.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformacoesDaSemanaService {
    private final ValidaUsuarioService validaUsuarioService;

    private final CustoRepository custoRepository;

    private final ReceitaDiariaRepository receitaDiariaRepository;

    private final UsuarioRepository usuarioRepository;

    public InformacoesDaSemanaResponse buscarInformacoesDaSemana(Long idUsuario){
        validaUsuarioService.porId(idUsuario);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true).get();

        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<ReceitaDiaria> receitas = receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(idUsuario, inicioSemana, hoje);
        List<Custo> custos = custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(usuario.getVeiculo().getIdVeiculo(), inicioSemana, hoje);

        Double ganhoBruto = receitas.stream()
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        Double despesas = custos.stream()
                .mapToDouble(Custo::getValor)
                .sum();

        Double lucroLiquido = ganhoBruto - despesas;

        double kmAtual = usuario.getVeiculo().getKmAtual();

        return InformacoesDaSemanaResponse.builder()
                .ganhoBruto(ganhoBruto)
                .despesaTotal(despesas)
                .lucroLiquido(lucroLiquido)
                .kmAtual(kmAtual)
                .build();
    }
}