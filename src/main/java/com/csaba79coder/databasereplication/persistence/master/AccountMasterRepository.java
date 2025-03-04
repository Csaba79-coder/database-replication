package com.csaba79coder.databasereplication.persistence.master;

import com.csaba79coder.databasereplication.entity.Account;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public interface AccountMasterRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findAccountByName(String name);
}
