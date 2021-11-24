package com.example.logreg.password;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ChangePassRequest {
    private String email;
    private String newPassword;
}
