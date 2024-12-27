package com.vigjoaopaulo.tictac_jornada.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vigjoaopaulo.tictac_jornada.model.Role;
import com.vigjoaopaulo.tictac_jornada.repository.RoleRepository;
import com.vigjoaopaulo.tictac_jornada.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    
   
    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll(); // Lógica para buscar todos os roles
    }
    
    @Override
    public Role salvar(Role role) {
        // Salvar a role no banco de dados e retornar a role salva
        return roleRepository.save(role);
    }

    @Override
    public Role buscarPorNome(String nome) {
        // Aqui, o método retorna um Optional<Role>, então usamos orElseThrow corretamente
        return roleRepository.findByRoleName(nome)
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + nome));
    }
}
