package com.example.logreg.registration;

import com.example.logreg.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;
    private ConfirmationTokenService confirmationTokenService;

    @RequestMapping(path = "api/v1/registration")
    public String register(@RequestBody RegistrationRequest request ){
        return registrationService.register(request);
    }

    @GetMapping("api/v1/registration/confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }

    @PostMapping("api/v1/registration/resetConfirmationEmail")
    public ResponseEntity<Integer> resendConfirmationEmail(@RequestParam("token") String token){
        return confirmationTokenService.resendConfirmationToken(token);
    }
}
