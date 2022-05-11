package com.usta.jwt.controller;

import com.usta.jwt.model.JwtRequest;
import com.usta.jwt.model.JwtResponse;
import com.usta.jwt.service.UserService;
import com.usta.jwt.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "Welcome User";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Pusooo mal las credenciales", e);
        }
        final UserDetails userDetails= userService.loadUserByUsername(jwtRequest.getUsername());
        final String token= jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }
}
