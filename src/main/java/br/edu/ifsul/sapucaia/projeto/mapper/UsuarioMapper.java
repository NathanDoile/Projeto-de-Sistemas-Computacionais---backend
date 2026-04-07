package br.edu.ifsul.sapucaia.projeto.mapper;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;

public class UsuarioMapper {
    private UsuarioMapper(){}

    public static Usuario toEntity(CadastrarUsuarioRequest cadastrarUsuarioRequest){
        return Usuario
                .builder()
                .nome(cadastrarUsuarioRequest.getNome())
                .email(cadastrarUsuarioRequest.getEmail())
                .senha(cadastrarUsuarioRequest.getSenha())
                .dataCadastro(cadastrarUsuarioRequest.getDataCadastro())
                .telefone(cadastrarUsuarioRequest.getTelefone())
                .build();
    }
}
