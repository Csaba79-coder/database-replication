package com.csaba79coder.databasereplication.persistence.master;

import com.csaba79coder.databasereplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserMasterRepository extends JpaRepository<User, UUID> {
}
