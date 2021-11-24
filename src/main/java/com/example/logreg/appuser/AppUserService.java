package com.example.logreg.appuser;

import com.example.logreg.registration.token.ConfirmationToken;
import com.example.logreg.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final String USER_NOT_FOUND_MESSAGE = "" +
            "user with email %s not found";

    private final AppUserRepository appUserRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE,email)));
    }

    public String signUp(AppUser appUser){
       boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

       if (userExists == true){
           throw new IllegalStateException("email already taken");
       }

       String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

       appUser.setPassword(encodedPassword);

       appUserRepository.save(appUser);

       String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO: send email.
        return token ;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
