package com.panels.SLcrud.repo;

import com.panels.SLcrud.model.Operation;
import com.panels.SLcrud.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findAllByOp(String op);

    List<Operation> findAllByUserId(Long userId);

}
