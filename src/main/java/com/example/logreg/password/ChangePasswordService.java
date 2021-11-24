package com.example.logreg.password;

import com.example.logreg.appuser.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChangePasswordService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public int changePassword(ChangePassRequest passRequest) {
        boolean userExist = appUserRepository.findByEmail(passRequest.getEmail()).isPresent();

        if (userExist == true){
            String encryptedPassword;
            encryptedPassword = bCryptPasswordEncoder.encode(passRequest.getNewPassword());
            return  appUserRepository.changeUserPassword(passRequest.getEmail(),encryptedPassword);
        }else {
            throw new IllegalStateException("user with email:" + passRequest.getEmail() + " does not exit");
        }


    }
}
