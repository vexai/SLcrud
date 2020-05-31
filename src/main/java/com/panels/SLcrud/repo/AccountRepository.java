package com.panels.SLcrud.repo;

import com.panels.SLcrud.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountById(Long id);
}
