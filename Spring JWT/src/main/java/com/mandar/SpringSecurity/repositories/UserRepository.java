package com.mandar.SpringSecurity.repositories;

import com.mandar.SpringSecurity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
