package com.usta.jwt.repositorio;

import com.usta.jwt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.username=?1")
    public Usuario findByUsername(String term);
}
