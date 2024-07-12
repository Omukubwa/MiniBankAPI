package com.compulynx.banking.auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "PinResets")
public class PinReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "custId", referencedColumnName = "id")
    private Customer customer;
    private String customerId;
    private String token;
    private boolean status;
    private LocalDateTime expiryDate;
}
