package com.compulynx.banking.auth.repositories;

import com.compulynx.banking.auth.models.PinReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PinReset,Long> {
    Optional<PinReset> findByCustomerIdAndToken(String customerId, String token);
    Optional<PinReset> findByCustomerId(String customerId);
}
