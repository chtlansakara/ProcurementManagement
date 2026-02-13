package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String username);

    //to create accounts
//    Optional<User> findByUserRole(UserRole userRole);

    List<User> findByUserRole(UserRole role);

    boolean existsByEmail(String email);
}
