package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.auth.JwtUtil;
import com.yicem.backend.yicem.dtos.response.LoginResponse;
import com.yicem.backend.yicem.models.User;
import com.yicem.backend.yicem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public LoginResponse login(String email, String password){
        /*Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByEmail(email));

        if (optionalUser.isEmpty())
            throw new BadCredentialsException("User not found");

        User user = optionalUser.get();

        if(password != user.getPassword())
            throw new BadCredentialsException("Bad credentials");

        String accessToken = jwtUtil.createToken(user);

        return LoginResponse.builder()
                .email(user.getEmail())
                .token(accessToken)
                .build();*/
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByEmail(email));
            User user = optionalUser.get();
            String token = jwtUtil.createToken(user);
            return LoginResponse.builder().email(user.getEmail()).token(token).build();
        }   catch (BadCredentialsException e){
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
