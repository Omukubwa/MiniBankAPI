package com.compulynx.banking.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String customerName;
    private String emailAddress;
    private String customerId;
    private String role;
}
