package com.example.covid.configuration.payload;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Setter
@Getter
public class LoginResponse implements Serializable {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
