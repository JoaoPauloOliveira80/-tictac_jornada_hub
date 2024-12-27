package com.vigjoaopaulo.tictac_jornada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TictacJornadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TictacJornadaApplication.class, args);
        getUser();
    }

    public static void getUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "123*";
        String senhaCodificada = encoder.encode(senha);
        System.out.println("Senha codificada: " + senhaCodificada);
    }
}
