package br.edu.ifsul.sapucaia.projeto.security.controller;

import br.edu.ifsul.sapucaia.projeto.security.controller.request.EnviarCodigoRequest;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.RedefinirSenhaRequest;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.ValidarCodigoRequest;
import br.edu.ifsul.sapucaia.projeto.security.service.EnviarCodigoService;
import br.edu.ifsul.sapucaia.projeto.security.service.RedefinirSenhaService;
import br.edu.ifsul.sapucaia.projeto.security.service.ValidarCodigoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class RedefinirSenhaController {

    private final EnviarCodigoService enviarCodigoService;
    private final ValidarCodigoService validarCodigoService;
    private final RedefinirSenhaService redefinirSenhaService;

    @PostMapping("/enviar-codigo")
    @ResponseStatus(HttpStatus.OK)
    public void enviarCodigo(@Valid @RequestBody EnviarCodigoRequest request) {
        enviarCodigoService.enviarCodigo(request);
    }

    @PostMapping("/validar-codigo")
    @ResponseStatus(HttpStatus.OK)
    public void validarCodigo(@Valid @RequestBody ValidarCodigoRequest request) {
        validarCodigoService.validar(request);
    }

    @PutMapping("/redefinir-senha")
    @ResponseStatus(HttpStatus.OK)
    public void redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {
        redefinirSenhaService.redefinirSenha(request);
    }
    
}