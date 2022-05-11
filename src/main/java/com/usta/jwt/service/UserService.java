package com.usta.jwt.service;

import com.usta.jwt.entity.Usuario;
import com.usta.jwt.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(@RequestBody String username) throws UsernameNotFoundException {
        Usuario us = usuarioRepositorio.findByUsername(username);
        String user = us.getUsername();
        String pass = us.getPassword();
        //Aqui se arregla la logica para obtener el usuario de la BD
        return new User(user, pass, new ArrayList<>());
    }
}
