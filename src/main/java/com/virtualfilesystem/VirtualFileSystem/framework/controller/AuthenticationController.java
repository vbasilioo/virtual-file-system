package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.domain.DTO.User.AuthenticationDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.DTO.User.LoginResponseDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.DTO.User.RegisterDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.model.User;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.UserRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.exception.ApiException;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.security.TokenService;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.utils.ReturnApi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var user = (User) auth.getPrincipal();
            var token = tokenService.generateToken(user);

            LoginResponseDTO response = new LoginResponseDTO(token, user);

            return ResponseEntity.ok(ReturnApi.success(response, "Login realizado com sucesso."));
        } catch (ApiException ex) {
            throw new ApiException(ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        try{
            if(this.userRepository.findByUsername(data.username()) != null) return ResponseEntity.ok(ReturnApi.error("Nome de usuário já existente."));

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User user = new User(data.username(), encryptedPassword, data.role());

            this.userRepository.save(user);
            return ResponseEntity.ok(ReturnApi.success(user, "Usuário criado com sucesso."));
        }catch(ApiException ex){
            throw new ApiException(ex.getMessage());
        }
    }
}
