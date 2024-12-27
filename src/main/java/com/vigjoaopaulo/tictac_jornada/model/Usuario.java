package com.vigjoaopaulo.tictac_jornada.model;

import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String senha;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String sobrenome;

	@Column(nullable = false)
	private String setor;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_role", // Nome da tabela associativa
			joinColumns = @JoinColumn(name = "usuario_id"), // Chave estrangeira para Usuario
			inverseJoinColumns = @JoinColumn(name = "role_id") // Chave estrangeira para Role
	)
	private Set<Role> roles; // Usando Set para roles

	@Column(nullable = false)
	private boolean ativo = true; // Definido como 'true' por padrão para marcar usuário como ativo

	// Construtores
	public Usuario() {
		// Construtor vazio
	}

	public Usuario(Long id, String email, String senha, String nome, String sobrenome, String setor, Set<Role> roles, boolean ativo) {
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.setor = setor;
		this.roles = roles;
		this.ativo = ativo;
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
