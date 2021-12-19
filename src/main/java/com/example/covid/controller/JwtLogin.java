package com.example.covid.controller;

import com.example.covid.configuration.core.CustomUserDetail;
import com.example.covid.configuration.jwt.JwtProvider;
import com.example.covid.configuration.payload.LoginRequest;
import com.example.covid.configuration.payload.LoginResponse;
import com.example.covid.entity.User;
import com.example.covid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class JwtLogin {
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
        return new LoginResponse(jwt);
    }
    @PostMapping("/registration")
    public ResponseEntity registrationUser(@Valid @RequestBody LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        User user = userRepository.findByUsername(username);
        if(user != null){
            return ResponseEntity.badRequest().body("Tài khoản này đã tồn tại");
        }else{
            String password = passwordEncoder.encode(loginRequest.getPassword());
            System.out.println(password);
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("user");
            userRepository.save(user);
            return ResponseEntity.ok().body("Tạo tài khoản thành công");
        }
    }

}
