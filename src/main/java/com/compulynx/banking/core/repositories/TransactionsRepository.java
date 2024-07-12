package com.compulynx.banking.core.repositories;

import com.compulynx.banking.core.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction,Long> {
    //List<Transaction> findFirst10ByAccountIdOrderByIdDesc(Long accountId);
    List<Transaction> findByTransactionCode(String tranCode);
}
