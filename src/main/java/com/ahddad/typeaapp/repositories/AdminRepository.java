package com.ahddad.typeaapp.repositories;

import com.ahddad.typeaapp.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    public Admin findByUsername(String username);
}
