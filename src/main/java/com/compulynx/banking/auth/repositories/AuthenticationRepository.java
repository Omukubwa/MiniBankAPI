/*
 * Copyright (c) 2024. 1905 OSS LTD
 */

package com.compulynx.banking.auth.repositories;

import com.compulynx.banking.auth.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthenticationRepository  extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerId(String customerId);
}

