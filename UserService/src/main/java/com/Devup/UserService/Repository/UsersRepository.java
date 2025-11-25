package com.Devup.UserService.Repository;

import com.Devup.UserService.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    boolean existsByEmail(String email);
    Users findByEmail(String email);
}
