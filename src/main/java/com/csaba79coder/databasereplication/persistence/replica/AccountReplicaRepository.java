package com.csaba79coder.databasereplication.persistence.replica;

import com.csaba79coder.databasereplication.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountReplicaRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findAccountByName(String name);
}
