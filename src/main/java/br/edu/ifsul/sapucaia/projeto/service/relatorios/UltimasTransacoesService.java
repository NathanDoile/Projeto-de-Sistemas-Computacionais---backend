package br.edu.ifsul.sapucaia.projeto.service.relatorios;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.UltimasTransacoesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UltimasTransacoesService {

    private final ValidaUsuarioService validaUsuarioService;
    private final CustoRepository custoRepository;
    private final ReceitaDiariaRepository receitaDiariaRepository;
    private final UsuarioRepository usuarioRepository;

    public List<UltimasTransacoesResponse> buscarUltimasTransacoes(Long idUsuario) {
        
        validaUsuarioService.porId(idUsuario);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true).get();
        Long idVeiculo = usuario.getVeiculo().getIdVeiculo();

        List<Custo> custos = custoRepository.findByVeiculoIdVeiculoAndIsAtivo(idVeiculo, true);

        List<ReceitaDiaria> receitas = receitaDiariaRepository.findByUsuarioIdUsuarioAndIsAtivo(idUsuario, true);

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