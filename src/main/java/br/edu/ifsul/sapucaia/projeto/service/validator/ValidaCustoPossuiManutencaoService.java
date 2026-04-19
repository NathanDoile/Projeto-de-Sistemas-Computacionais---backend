package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class ValidaCustoPossuiManutencaoService {

    private final CustoRepository custoRepository;

    public void porId(Long idCusto){

        Custo custo = custoRepository.findById(idCusto).get();

        if(!isNull(custo.getManutencao())){
            throw new ResponseStatusException(CONFLICT, "Custo já possui uma manutenção.");
        }
    }
}
