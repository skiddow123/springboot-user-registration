package com.example.logreg.password;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangePasswordController {

    private final ChangePasswordService changePasswordService;

    public ChangePasswordController(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    @PostMapping("/api/v1/changepassword")
    public int changePassword(@RequestBody ChangePassRequest passRequest){
        System.out.println(passRequest.getEmail()+ "----" + passRequest.getNewPassword());

        return changePasswordService.changePassword(passRequest);
    }
}
