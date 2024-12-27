package com.vigjoaopaulo.tictac_jornada.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.vigjoaopaulo.tictac_jornada.model.Usuario;

public class UsuarioDto {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private Set<String> roles; // Se você estiver usando um Set para roles

    // Construtor padrão
    public UsuarioDto() {}

    // Construtor que recebe um objeto Usuario
    public UsuarioDto(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.sobrenome = usuario.getSobrenome();
        this.email = usuario.getEmail();
        this.roles = usuario.getRoles().stream()
                          .map(role -> role.getRoleName())
                          .collect(Collectors.toSet());
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
