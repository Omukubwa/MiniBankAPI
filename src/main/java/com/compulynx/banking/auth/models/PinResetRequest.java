package com.compulynx.banking.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinResetRequest {
    private String customerId;
    private String token;
}
