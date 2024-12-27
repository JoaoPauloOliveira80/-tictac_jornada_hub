package com.vigjoaopaulo.tictac_jornada.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vigjoaopaulo.tictac_jornada.dto.UsuarioDto;
import com.vigjoaopaulo.tictac_jornada.model.Role;
import com.vigjoaopaulo.tictac_jornada.model.Usuario;
import com.vigjoaopaulo.tictac_jornada.service.RoleService;
import com.vigjoaopaulo.tictac_jornada.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;
	private Long idUser;
	private String email;

	@Autowired
	private PasswordEncoder passwordEncoder; // Injeção do PasswordEncoder

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping("/welcome")
	public ModelAndView TelaBemVindo(Model model) {

		// Retorna o template "bemvindo.html"
		return new ModelAndView("welcome/welcome");
	}
//
//	@GetMapping("/login")
//	public ModelAndView login() {
//		return new ModelAndView("login");
//	}

	@GetMapping("/login")
	public ModelAndView login(
	        @RequestParam(value = "error", required = false) String error,
	        @RequestParam(value = "logout", required = false) String logout) {
	    
	    ModelAndView modelAndView = new ModelAndView("login"); // Nome do arquivo HTML da página de login

	    if ("inactive".equals(error)) {
	        modelAndView.addObject("errorMessage", "Acesso negado: Sua conta não está ativa.");
	        System.out.println("Acesso negado");
	    } else if ("invalid".equals(error)) {
	        modelAndView.addObject("errorMessage", "Usuário ou senha inválidos.");
	        System.out.println("Usuário ou senha inválidos.");
	    }

	    if (logout != null) {
	        modelAndView.addObject("logoutMessage", "Você saiu com sucesso.");
	        System.out.println("Logout realizado.");
	    }

	    return modelAndView;
	}


	@GetMapping("/cadastro")
	public ModelAndView mostrarFormularioDeCadastro() {
		ModelAndView modelAndView = new ModelAndView("cadastro_user/cadastro_user");
		modelAndView.addObject("usuario", new Usuario());
		return modelAndView;
	}

	@GetMapping("/validationEmail")
	public ModelAndView validationEmail() {
		ModelAndView modelAndView = new ModelAndView("email/validation_email");
		modelAndView.addObject("usuario", new Usuario());
		return modelAndView;
	}

	@PostMapping("/desativar")
	public String desativarConta(RedirectAttributes redirectAttributes) {
		String email = getEmail();

		if (usuarioService.isUsuarioAtivo(email)) {
			try {
				usuarioService.desativarUsuarioPorEmail(email);
				redirectAttributes.addFlashAttribute("message", "Conta desativada com sucesso.");
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("error", "Erro ao desativar conta.");
			}
			return "redirect:/welcome/welcome"; // Ou outra página de destino
		}

		// Redireciona caso o usuário não esteja ativo
		redirectAttributes.addFlashAttribute("error", "Acesso negado: Usuário não está ativo.");
		return "redirect:/login"; // Redireciona para a página de login ou outra página apropriada
	}

	@GetMapping("/pag_user")
	public ModelAndView telaUpdateUserId() {
		Usuario usuario = usuarioService.findById(idUser); // Obtém o usuário do banco de dados

		// Debugging com System.out.println
		System.out.println("ID do Usuário: " + usuario.getId());
		System.out.println("Nome: " + usuario.getNome());
		System.out.println("Sobrenome: " + usuario.getSobrenome());
		System.out.println("Email: " + usuario.getEmail());
		System.out.println("Setor: " + usuario.getSetor());

		ModelAndView modelAndView = new ModelAndView("cadastro_user/update_user");
		modelAndView.addObject("usuario", usuario); // Passa o usuário para o formulário
		return modelAndView;
	}

	@GetMapping("/admin")
	public ModelAndView telaAdmin(Model model) {
		// Obtemos o email do usuário autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		// Buscamos o usuário no banco de dados usando o email
		UsuarioDto usuarioDto = usuarioService.buscarPorEmail(email);

		// Pega id do Usuario autenticado
		idUser = usuarioDto.getId();
		// Exibir os dados do usuário no console
		System.out.println("Dados do usuário (Admin):");
		System.out.println("Id: " + idUser);
		System.out.println("Nome: " + usuarioDto.getNome());
		System.out.println("Sobrenome: " + usuarioDto.getSobrenome());
		System.out.println("Email: " + usuarioDto.getEmail());

		// Adicionamos o DTO do usuário ao modelo
		model.addAttribute("usuario", usuarioDto);

		return new ModelAndView("admin");
	}

	// Método POST para atualizar o usuário
	@PostMapping("/atualizar")
	public String atualizarUsuario(@ModelAttribute Usuario usuario) {
		usuarioService.update(usuario); // Chama o serviço para atualizar o usuário
		return "redirect:/usuarios/pag_user/" + usuario.getId(); // Redireciona para a página do usuário atualizado
	}
//	@PostMapping("/cadastro")
//	public String processarFormularioDeCadastro(@ModelAttribute("usuario") Usuario usuario, Model model) {
//	    try {
//	        // Codificar a senha antes de salvar
//	        String senhaCodificada = passwordEncoder.encode(usuario.getSenha());
//	        usuario.setSenha(senhaCodificada);
//
//	        // Atribuir a role "ROLE_USER" ao usuário
//	        Role role = roleService.buscarPorNome("USER"); // Buscando a role no banco
//	        usuario.setRoles(Collections.singleton(role)); // Atribuindo a role ao usuário
//
//	        // Salvar o usuário no banco
//	        usuarioService.salvar(usuario);
//
//	        // Verificação se o usuário foi salvo corretamente
//	        if (usuario.getId() != null) {
//	            model.addAttribute("mensagem", "Usuário cadastrado com sucesso!");
//	            return "redirect:/usuarios/login"; // Redirecionar para a página de login
//	        } else {
//	            model.addAttribute("mensagem", "Erro ao cadastrar o usuário.");
//	            return "cadastro"; // Voltar para o formulário de cadastro
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace(); // Adicione logs para entender o erro
//	        model.addAttribute("mensagem", "Erro ao processar o cadastro.");
//	        return "cadastro"; // Voltar para o formulário de cadastro
//	    }
//	}

	@PostMapping("/cadastro")
	public ModelAndView processarFormularioDeCadastro(@ModelAttribute("usuario") Usuario usuario,
			RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			// Codificar a senha antes de salvar
			String senhaCodificada = passwordEncoder.encode(usuario.getSenha());
			usuario.setSenha(senhaCodificada);

			// Atribuir a role "ROLE_USER" ao usuário
			Role role = roleService.buscarPorNome("USER"); // Buscando a role no banco
			usuario.setRoles(Collections.singleton(role)); // Atribuindo a role ao usuário

			// Definir o campo 'ativo' como true no momento do cadastro
			usuario.setAtivo(true);

			// Salvar o usuário no banco
			usuarioService.salvar(usuario);

			// Verificação se o usuário foi salvo corretamente
			if (usuario.getId() != null) {
				redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
				modelAndView.setViewName("redirect:/usuarios/login"); // Redirecionar para login
			} else {
				redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar o usuário.");
				modelAndView.setViewName("redirect:/usuarios/cadastro"); // Voltar para o formulário de cadastro
			}

		} catch (Exception e) {
			e.printStackTrace(); // Adicione logs para entender o erro
			redirectAttributes.addFlashAttribute("mensagem", "Erro ao processar o cadastro.");
			modelAndView.setViewName("redirect:/usuarios/cadastro"); // Voltar para o formulário de cadastro
		}
		return modelAndView;
	}

	public Long getId() {
		return idUser;
	}

	public String getEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName(); // Retorna o email do usuário autenticado
	}

	@GetMapping("/user")
	public ModelAndView telaUser(Model model) {
		// Obtemos o email do usuário autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		// Buscamos o usuário no banco de dados usando o email
		UsuarioDto usuarioDto = usuarioService.buscarPorEmail(email);

		email = usuarioDto.getEmail();

		// Exibir os dados do usuário no console
		System.out.println("Dados do usuário (User):");
		idUser = usuarioDto.getId();
		System.out.println("Id: " + idUser);
		System.out.println("Nome: " + usuarioDto.getNome());
		System.out.println("Sobrenome: " + usuarioDto.getSobrenome());
		System.out.println("Email: " + usuarioDto.getEmail());

		// Adicionamos o DTO do usuário ao modelo
		model.addAttribute("usuario", usuarioDto);

		return new ModelAndView("user");
	}

	@GetMapping("/adeus")
	public ModelAndView telaAteLogo() {

		return new ModelAndView("adeus");
	}

	@GetMapping("/logout")
	public ModelAndView telaTogout(Model model) {
		return new ModelAndView("logout");
	}

	@Autowired
	private RoleService roleService;

}
