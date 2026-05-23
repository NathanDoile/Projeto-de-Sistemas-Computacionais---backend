package br.edu.ifsul.sapucaia.projeto.service.veiculo;

import br.edu.ifsul.sapucaia.projeto.controller.request.veiculo.AtualizarKmVeiculoRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaKmAtualizadoVeiculoValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AtualizarKmVeiculoService {

    private final VeiculoRepository veiculoRepository;

    private final ValidaUsuarioService validaUsuarioService;

    private final ValidaKmAtualizadoVeiculoValidator validaKmAtualizadoVeiculoValidator;

    public void atualizar(@Valid AtualizarKmVeiculoRequest atualizarKmVeiculoRequest) {

        validaUsuarioService.porId(atualizarKmVeiculoRequest.getIdUsuario());

        Veiculo veiculo = veiculoRepository.findByUsuarioIdUsuarioAndIsAtivo(atualizarKmVeiculoRequest.getIdUsuario(), true);

        validaKmAtualizadoVeiculoValidator.maiorQueAtual(atualizarKmVeiculoRequest.getKmAtualizado(), veiculo.getKmAtual());

        veiculo.setKmAtual(atualizarKmVeiculoRequest.getKmAtualizado());

        veiculoRepository.save(veiculo);
    }
}
