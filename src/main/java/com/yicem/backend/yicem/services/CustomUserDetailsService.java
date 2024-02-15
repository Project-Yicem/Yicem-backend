package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.User;
import com.yicem.backend.yicem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    /*
       TODO authentication çalışıyor ama swagger'da apilere erişmek
        için oluşturulan user'ın email ve passwordünün girilmesi lazım.
        Swagger'ı developerlar için özel oluşturulmuş username ve passwordle
        erişebilmesi için config edilmesi lazım.
     */
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findUserByEmail(email);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }
}
