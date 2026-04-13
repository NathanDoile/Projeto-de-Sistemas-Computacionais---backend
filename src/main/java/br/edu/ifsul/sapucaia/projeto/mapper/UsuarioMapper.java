package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;

public class UsuarioMapper {
    private UsuarioMapper(){}

    public static Usuario toEntity(CadastrarUsuarioRequest cadastrarUsuarioRequest){
        return Usuario
                .builder()
                .nome(cadastrarUsuarioRequest.getNome())
                .email(cadastrarUsuarioRequest.getEmail())
                .senha(cadastrarUsuarioRequest.getSenha())
                .telefone(cadastrarUsuarioRequest.getTelefone())
                .build();
    }

    public static void updateEntity(Usuario usuario, EditarPerfilUsuarioRequest editarPerfilUsuarioRequest){
        if (editarPerfilUsuarioRequest.getNome() != null) {
            usuario.setNome(editarPerfilUsuarioRequest.getNome());
        }
        if (editarPerfilUsuarioRequest.getEmail() != null) {
            usuario.setEmail(editarPerfilUsuarioRequest.getEmail());
        }
        if (editarPerfilUsuarioRequest.getTelefone() != null) {
            usuario.setTelefone(editarPerfilUsuarioRequest.getTelefone());
        }
    }

}
