package com.csaba79coder.databasereplication.persistence.master;

import com.csaba79coder.databasereplication.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogMasterRepository extends JpaRepository<AuditLog, UUID> {
}
