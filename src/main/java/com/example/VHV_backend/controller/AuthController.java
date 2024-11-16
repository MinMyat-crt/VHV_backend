package com.example.VHV_backend.controller;

import com.example.VHV_backend.Dto.AuthResponseDto;
import com.example.VHV_backend.Dto.LoginDto;
import com.example.VHV_backend.Dto.SignupDto;
import com.example.VHV_backend.model.Role;
import com.example.VHV_backend.model.UserEntity;
import com.example.VHV_backend.repo.RoleRespository;
import com.example.VHV_backend.repo.UserRepository;
import com.example.VHV_backend.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
@CrossOrigin
@RestController
@RequestMapping("api/")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRespository roleRespository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RoleRespository roleRespository, UserRepository userRepository,JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRespository = roleRespository;
        this.userRepository = userRepository;
        this.jwtGenerator=jwtGenerator;
    }
    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token),HttpStatus.OK);

    }
    @PostMapping("signup")
    public ResponseEntity<String > signup(@RequestBody SignupDto signupDto){
        if(userRepository.existsByUsername(signupDto.getUsername())){
            return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user=new UserEntity();
        user.setUsername(signupDto.getUsername());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        Role roles=roleRespository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);
        return new ResponseEntity<>("User sign up success",HttpStatus.OK);
    }
}
