package br.edu.ifsul.sapucaia.projeto.service.manutencao;

import br.edu.ifsul.sapucaia.projeto.domain.Manutencao;
import br.edu.ifsul.sapucaia.projeto.repository.ManutencaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListarManutencaoService {

    private final ManutencaoRepository manutencaoRepository;

    public Page<Manutencao> listar(Pageable pageable) {
        return manutencaoRepository.findAllByIsAtivo(true, pageable);
    }
}