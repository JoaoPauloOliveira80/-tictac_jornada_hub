package com.vigjoaopaulo.tictac_jornada.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.vigjoaopaulo.tictac_jornada.model.Usuario;

public class UsuarioToDto {

    // Converte uma entidade Usuario para um DTO sem senha
    public static UsuarioDto toDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setEmail(usuario.getEmail());
        // Adicione os papéis se necessário
        return dto;
    }

    // Converte um DTO de cadastro para uma entidade Usuario
    public static Usuario toEntity(UsuarioCadastroDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setSobrenome(dto.getSobrenome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha()); // A senha é importante apenas ao criar o usuário
        return usuario;
    }

    // Converte uma lista de entidades Usuario para uma lista de DTOs
    public static List<UsuarioDto> toDtoList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioToDto::toDto)
                .collect(Collectors.toList());
    }

    // Converte uma lista de DTOs de cadastro para uma lista de entidades Usuario
    public static List<Usuario> toEntityList(List<UsuarioCadastroDto> dtos) {
        return dtos.stream()
                .map(UsuarioToDto::toEntity)
                .collect(Collectors.toList());
    }
}
