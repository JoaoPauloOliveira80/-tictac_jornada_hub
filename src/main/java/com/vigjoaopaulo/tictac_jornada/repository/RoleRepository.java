package com.vigjoaopaulo.tictac_jornada.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vigjoaopaulo.tictac_jornada.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();
    
    Optional<Role> findByRoleName(String roleName); // Retorna um Optional<Role>
     
}
