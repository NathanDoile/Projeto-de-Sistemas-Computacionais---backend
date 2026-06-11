package br.edu.ifsul.sapucaia.projeto.security.controller;

import br.edu.ifsul.sapucaia.projeto.controller.response.LoginUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.security.service.BuscarUsuarioLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private BuscarUsuarioLoginService buscarUsuarioLoginService;

    @PostMapping
    public LoginUsuarioResponse login(){
        return buscarUsuarioLoginService.buscar();
    }

}
