package com.vigjoaopaulo.tictac_jornada.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vigjoaopaulo.tictac_jornada.model.Role;

@Service
public interface RoleService {


    List<Role> findAllRoles(); // Método para retornar todos os papéis
    
    Role salvar(Role role);  // Método para salvar uma role
    Role buscarPorNome(String nome);  // Método para buscar uma role pelo nome

}
