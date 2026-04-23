package br.edu.ifsul.sapucaia.projeto.factory;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;

public class UsuarioFactory {
    public static Usuario usuario() {

        return Usuario
                .builder()
                .idUsuario(1L)
                .isAtivo(true)
                .build();
    }
}
