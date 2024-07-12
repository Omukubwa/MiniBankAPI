package com.compulynx.banking.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    public String accessToken;
    public Object expiresIn;
    public String subject;
    public Object issuedAt;
    public String tokenType = "Bearer";
    public Object body;
}
