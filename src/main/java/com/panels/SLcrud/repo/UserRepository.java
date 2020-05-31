package com.panels.SLcrud.repo;


import com.panels.SLcrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserName(String userName);
    User findUserById(Long id);
    List<User> findAllByRoles_Role(String role);
}
