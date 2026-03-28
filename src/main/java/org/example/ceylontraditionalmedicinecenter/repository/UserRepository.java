package org.example.ceylontraditionalmedicinecenter.repository;

import org.example.ceylontraditionalmedicinecenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    List<User> findAllByStatus(String status);

    long countByStatus(String status);

    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' OR u.status IS NULL OR u.status = ''")
    List<User> findActiveOrUnspecifiedStatusUsers();

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE' OR u.status IS NULL OR u.status = ''")
    long countActiveOrUnspecifiedStatusUsers();
}
