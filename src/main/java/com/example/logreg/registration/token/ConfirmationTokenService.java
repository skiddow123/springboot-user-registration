package com.example.logreg.registration.token;

import com.example.logreg.appuser.AppUser;
import com.example.logreg.email.EmailSender;
import com.example.logreg.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService  {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
       Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        return confirmationToken;
    }

    public int setConfirmedAt(String token) {
       return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    @Transactional
    public ResponseEntity<Integer> resendConfirmationToken(String oldToken){
        ConfirmationToken token = getToken(oldToken).orElseThrow( () -> new IllegalStateException("token not found"));
//        boolean confirmed = token.getConfirmedAt();
        if (token.getConfirmedAt() != null ){
            throw new IllegalStateException("Email already verified");
        }else {
            String newToken = UUID.randomUUID().toString();
            int update = confirmationTokenRepository.updateToken(oldToken, newToken , LocalDateTime.now(),LocalDateTime.now().plusMinutes(15));
            AppUser appUser = token.getAppUser();
            String link = "localhost:8080/api/v1/registration/confirm?token=" + newToken;
            emailSender.send(appUser.getEmail(),emailSender.buildEmail(appUser.getFirstName(),link));
            return new ResponseEntity<>(update, HttpStatus.OK);
        }
    }
}
