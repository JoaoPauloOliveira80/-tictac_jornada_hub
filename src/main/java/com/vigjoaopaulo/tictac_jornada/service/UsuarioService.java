package com.vigjoaopaulo.tictac_jornada.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.vigjoaopaulo.tictac_jornada.dto.UsuarioCadastroDto;
import com.vigjoaopaulo.tictac_jornada.dto.UsuarioDto;
import com.vigjoaopaulo.tictac_jornada.model.Usuario;

public interface UsuarioService extends UserDetailsService {

	UsuarioDto cadastrarUsuario(UsuarioCadastroDto dto);

	Usuario update(Usuario usuario); // Método para atualizar usuário

	List<UsuarioDto> listarUsuarios();

	UsuarioDto buscarPorEmail(String email);

	Usuario findById(Long id);

	void salvar(Usuario usuario); // Declaração do método salvar

	boolean isEmailExistente(String email);

	void salvarToken(String email, String token); // Método para salvar o token

	boolean validarToken(String email, String token); // Método para validar o token
	
    void desativarUsuarioPorEmail(String email); // Definindo o método na interface

    boolean isUsuarioAtivo(String email); // Novo método para verificar se o usuário está ativo
}
