package com.example.covid.configuration.payload;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LoginRequest implements Serializable {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
