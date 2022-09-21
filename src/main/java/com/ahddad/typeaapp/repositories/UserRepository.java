package com.ahddad.typeaapp.repositories;

import com.ahddad.typeaapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmail(String email);
    public User findByVerificationCode(String code);
}
