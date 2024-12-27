package com.vigjoaopaulo.tictac_jornada.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vigjoaopaulo.tictac_jornada.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
	Optional<Usuario> findByEmail(String email);
	
    Usuario findBynome(String nome); // Método para buscar usuário pelo nome de usuário

}
