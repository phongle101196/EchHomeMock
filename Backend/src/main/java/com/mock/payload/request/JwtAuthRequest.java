package com.mock.payload.request;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String username;
    private String password;
}
