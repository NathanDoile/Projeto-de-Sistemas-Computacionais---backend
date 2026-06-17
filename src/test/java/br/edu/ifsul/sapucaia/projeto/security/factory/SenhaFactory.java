package br.edu.ifsul.sapucaia.projeto.security.factory;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.EnviarCodigoRequest;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.RedefinirSenhaRequest;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.ValidarCodigoRequest;

public class SenhaFactory {

    public static RedefinirSenhaRequest redefinirSenhaRequest() {
        RedefinirSenhaRequest request = new RedefinirSenhaRequest();
        request.setEmail("emailteste@gmail.com");
        request.setCodigo("123456");
        request.setSenha("novaSenha123");
        return request;
    }

    public static EnviarCodigoRequest enviarCodigoRequest() {
        EnviarCodigoRequest request = new EnviarCodigoRequest();
        request.setEmail("emailteste@gmail.com");
        return request;
    }

    public static ValidarCodigoRequest validarCodigoRequest() {
        ValidarCodigoRequest request = new ValidarCodigoRequest();
        request.setEmail("emailteste@gmail.com");
        request.setCodigo("123456");
        return request;
    }

    public static Usuario usuarioComCodigo(String codigo, int tentativas) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setEmail("emailteste@gmail.com");
        usuario.setAtivo(true);
        usuario.setCodigoRedefinirSenha(codigo);
        usuario.setTentativasRedefinirSenha(tentativas);
        usuario.setSenha("senhaAntiga123");
        return usuario;
    }
}