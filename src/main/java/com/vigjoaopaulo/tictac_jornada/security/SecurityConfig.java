package com.vigjoaopaulo.tictac_jornada.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        
        	.authorizeHttpRequests(authz -> authz
        	    .requestMatchers(
        	        "/usuarios/welcome",       // Página de boas-vindas
        	        "/usuarios/login",         // Página de login
        	        "/usuarios/admin",         // Página de admin
        	        "/usuarios/user",          // Página de usuário
        	        "/usuarios/cadastro",      // Página de cadastro
        	        "/usuarios/pag_user",      // Página de usuário específico
        	        "/usuarios/atualizar",     // Página de atualização de usuário
        	        "/usuarios/desativar"      // Página de desativação de usuário
        	    ).permitAll() // Permite acesso público a essas páginas
        	    .anyRequest().authenticated() // Requer autenticação para todas as outras páginas
        	)
            .formLogin(form -> form
                .loginPage("/usuarios/login") // Página de login personalizada
                .loginProcessingUrl("/usuarios/login") // Endpoint para processar o login
                .permitAll() // Permite acesso público ao login
                .successHandler(authenticationSuccessHandler()) // Define o handler de sucesso de login
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/usuarios/adeus") // Redireciona para a página "adeus" após logout
                .permitAll() // Permite acesso público ao logout
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();

                if ("ROLE_ADMIN".equals(role)) {
                    response.sendRedirect("/usuarios/admin"); // Redireciona para a página admin
                    return;
                }
                if ("ROLE_USER".equals(role)) {
                    response.sendRedirect("/usuarios/user"); // Redireciona para a página user
                    
                    return;
                }
            }

            // Redireciona para uma página padrão caso nenhum papel seja encontrado
            response.sendRedirect("/usuarios/bemvindo");
        };
    }
}
