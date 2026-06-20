package br.edu.ifsul.sapucaia.projeto.service.relatorios;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.UltimasTransacoesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UltimasTransacoesService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final CustoRepository custoRepository;
    private final ReceitaDiariaRepository receitaDiariaRepository;

    public List<UltimasTransacoesResponse> buscarUltimasTransacoes() {

        UsuarioSecurity usuarioSecurity = usuarioAutenticadoService.getUser();

        List<Custo> custos = custoRepository.findByVeiculoIdVeiculo(usuarioSecurity.getIdVeiculo());

        List<ReceitaDiaria> receitas = receitaDiariaRepository.findByUsuarioIdUsuario(usuarioSecurity.getId());

        List<UltimasTransacoesResponse> transacoes = new ArrayList<>();

        custos.forEach(custo -> transacoes.add(UltimasTransacoesResponse.builder()
                .descricao(null)
                .categoria(custo.getTipo())
                .valor(custo.getValor())
                .data(custo.getDataPagamento())
                .tipoTransacao("CUSTO")
                .build()));

        receitas.forEach(receita -> transacoes.add(UltimasTransacoesResponse.builder()
                .descricao("Receita de Corrida")
                .categoria(null)
                .valor(receita.getValor())
                .data(receita.getDataReceita())
                .tipoTransacao("RECEITA") 
                .build()));

        return transacoes.stream()
                .sorted(Comparator.comparing(UltimasTransacoesResponse::getData).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}