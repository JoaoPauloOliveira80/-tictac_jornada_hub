package com.vigjoaopaulo.tictac_jornada.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.vigjoaopaulo.tictac_jornada.dto.UsuarioCadastroDto;
import com.vigjoaopaulo.tictac_jornada.dto.UsuarioDto;
import com.vigjoaopaulo.tictac_jornada.model.Usuario;
import com.vigjoaopaulo.tictac_jornada.repository.UsuarioRepository;
import com.vigjoaopaulo.tictac_jornada.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	private final UsuarioRepository usuarioRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final Map<String, String> tokenStorage = new HashMap<>(); // Temporário (usar cache ou banco em produção)

	@Autowired
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public boolean isUsuarioAtivo(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o email: " + email));
		return usuario.isAtivo();
	}

	@Override
	public UsuarioDto cadastrarUsuario(UsuarioCadastroDto dto) {
		logger.info("Iniciando cadastro do usuário com email: {}", dto.getEmail());

		if (isEmailExistente(dto.getEmail())) {
			logger.warn("Tentativa de cadastro com email já existente: {}", dto.getEmail());
			throw new IllegalArgumentException("O email já está em uso: " + dto.getEmail());
		}

		Usuario usuario = new Usuario();
		usuario.setEmail(dto.getEmail());
		usuario.setNome(dto.getNome());
		usuario.setSobrenome(dto.getSobrenome());
		usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

		Usuario salvo = usuarioRepository.save(usuario);
		logger.info("Usuário cadastrado com sucesso: {}", salvo.getEmail());

		return new UsuarioDto(salvo);
	}

	@Override
	public List<UsuarioDto> listarUsuarios() {
		logger.info("Listando todos os usuários cadastrados.");
		return usuarioRepository.findAll().stream().map(UsuarioDto::new).collect(Collectors.toList());
	}

	@Override
	public UsuarioDto buscarPorEmail(String email) {
		logger.info("Buscando usuário com email: {}", email);

		Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> {
			logger.error("Usuário não encontrado com o email: {}", email);
			return new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
		});

		logger.info("Usuário encontrado: {}", usuario.getEmail());
		return new UsuarioDto(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    logger.info("Carregando detalhes do usuário pelo email: {}", username);
	    

	    Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow(() -> {
	        logger.error("Usuário não encontrado com o email: {}", username);
	        return new UsernameNotFoundException("Acesso negado: Sua conta não existe mais no banco de dados.");
	    });

	    boolean isAtivo = usuario.isAtivo();
	    List<GrantedAuthority> authorities = null;
	    if (isAtivo) {
	        authorities = usuario.getRoles().stream()
	                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
	                .collect(Collectors.toList());

	        logger.info("Detalhes do usuário carregados com sucesso.");
	    } else {
	    	
	        logger.warn("ACESSO NEGADO: Usuário inativo.");
	        throw new UsernameNotFoundException("Acesso negado: Sua conta não está ativa.");
	    }

	    return new org.springframework.security.core.userdetails.User(
	            usuario.getEmail(),
	            usuario.getSenha(),
	            true, // Account non-expired
	            true, // Credentials non-expired
	            true, // Account non-locked
	            true, // Account enabled
	            authorities
	    );
	}



//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//	    logger.info("Carregando detalhes do usuário pelo email: {}", username);
//
//	    Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow(() -> {
//	        logger.error("Usuário não encontrado com o email: {}", username);
//	        return new UsernameNotFoundException("Acesso negado: Sua conta não existe mais no banco de dados.");
//	    });
//
//	    if (!usuario.isAtivo()) {
//	        logger.warn("Tentativa de login com usuário inativo: {}", username);
//	        throw new UsernameNotFoundException("Acesso negado: Sua conta não está ativa.");
//	    }
//
//	    List<GrantedAuthority> authorities = usuario.getRoles().stream()
//	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
//	            .collect(Collectors.toList());
//
//	    logger.info("Detalhes do usuário carregados com sucesso.");
//	    return new org.springframework.security.core.userdetails.User(
//	            usuario.getEmail(),
//	            usuario.getSenha(),
//	            true, // Account non-expired
//	            true, // Credentials non-expired
//	            true, // Account non-locked
//	            usuario.isAtivo(), // Account enabled
//	            authorities
//	    );
//	}

	@Override
	public void salvar(Usuario usuario) {
		logger.info("Salvando usuário com email: {}", usuario.getEmail());
		usuarioRepository.save(usuario);
	}

	@Override
	public Usuario findById(Long id) {
		logger.info("Buscando usuário com ID: {}", id);
		return usuarioRepository.findById(id).orElseThrow(() -> {
			logger.error("Usuário não encontrado com o ID: {}", id);
			return new RuntimeException("Usuário não encontrado");
		});
	}

	@Override
	public Usuario update(Usuario usuario) {
		logger.info("Atualizando usuário com ID: {}", usuario.getId());

		Usuario usuarioExistente = usuarioRepository.findById(usuario.getId()).orElseThrow(() -> {
			logger.error("Usuário não encontrado com o ID: {}", usuario.getId());
			return new IllegalArgumentException("Usuário não encontrado com o ID: " + usuario.getId());
		});

		usuarioExistente.setNome(usuario.getNome());
		usuarioExistente.setSobrenome(usuario.getSobrenome());
		usuarioExistente.setEmail(usuario.getEmail());
		usuarioExistente.setSetor(usuario.getSetor());

		if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
			usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}

		Usuario atualizado = usuarioRepository.save(usuarioExistente);
		logger.info("Usuário atualizado com sucesso: {}", atualizado.getId());
		return atualizado;
	}

	@Override
	public boolean isEmailExistente(String email) {
		logger.info("Verificando se o email já está cadastrado: {}", email);
		return usuarioRepository.findByEmail(email).isPresent();
	}

	@Override
	public void salvarToken(String email, String token) {
		logger.info("Salvando token para o email: {}", email);
		tokenStorage.put(email, token);
	}

	@Override
	public boolean validarToken(String email, String token) {
		logger.info("Validando token para o email: {}", email);
		return token.equals(tokenStorage.get(email));
	}

	// Implementação do método desativarUsuarioPorEmail
	@Override
	public void desativarUsuarioPorEmail(String email) {
		// Busca o usuário pelo email
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o email: " + email));

		// Marca o usuário como inativo (ajuste conforme o seu campo para ativo/inativo)
		usuario.setAtivo(false);

		// Salva a alteração no banco de dados
		usuarioRepository.save(usuario);
	}

}
