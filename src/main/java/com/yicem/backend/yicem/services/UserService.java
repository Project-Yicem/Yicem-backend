package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.User;
import com.yicem.backend.yicem.payload.request.PasswordChangeRequest;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public String getIdFromHeader(HttpHeaders header) {
        if(header.get("Authorization") != null){
            String token = header.get("Authorization").get(0);

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7, token.length());
                return jwtUtils.getIdFromJwtToken(jwtToken);
            }
        }

        return "";
    }

    public ResponseEntity<?> changePassword(HttpHeaders header, PasswordChangeRequest passwordChangeRequest) {

        String userId = getIdFromHeader(header);
        if(userId.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        Optional<User> userInstance = userRepository.findById(userId);

        if(userInstance.isPresent()){
            User user = userInstance.get();
            String dbPassword = user.getPassword();
            String oldPassword = passwordChangeRequest.getOldPassword();
            String newPassword = passwordChangeRequest.getNewPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(oldPassword, dbPassword)){
                newPassword = encoder.encode(newPassword);
                user.setPassword(newPassword);
                userRepository.save(user);
                return ResponseEntity.ok("Password changed");
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Error: Old password does not match"));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
        }
    }

}
