package com.virtualfilesystem.VirtualFileSystem.framework.controller;

import com.virtualfilesystem.VirtualFileSystem.domain.DTO.User.AuthenticationDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.DTO.User.RegisterDTO;
import com.virtualfilesystem.VirtualFileSystem.domain.model.User;
import com.virtualfilesystem.VirtualFileSystem.domain.model.UserRole;
import com.virtualfilesystem.VirtualFileSystem.domain.repository.UserRepository;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.security.TokenService;
import com.virtualfilesystem.VirtualFileSystem.infrastructure.utils.ReturnApi;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.xml.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = (Validator) Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testLoginSuccess() {
        AuthenticationDTO authDTO = new AuthenticationDTO("username", "password");
        User mockUser = new User("username", "encryptedPassword", UserRole.USER);
        var token = "mockedToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null));
        when(tokenService.generateToken(mockUser)).thenReturn(token);

        ResponseEntity<?> response = authenticationController.login(authDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login realizado com sucesso.", ((ReturnApi) response.getBody()).getMessage());
    }

    @Test
    void testRegisterSuccess() {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "password", UserRole.USER);
        when(userRepository.findByUsername(registerDTO.username())).thenReturn(null);

        ResponseEntity<?> response = authenticationController.register(registerDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuário criado com sucesso.", ((ReturnApi) response.getBody()).getMessage());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("existingUser", "password", UserRole.USER);
        when(userRepository.findByUsername(registerDTO.username())).thenReturn(new User("existingUser", "encryptedPassword", UserRole.USER));

        ResponseEntity<?> response = authenticationController.register(registerDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Nome de usuário já existente.", ((ReturnApi<Object>) response.getBody()).getMessage());
    }
}
