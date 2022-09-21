package com.ahddad.typeaapp.repositories;

import com.ahddad.typeaapp.models.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<CustomUserDetails, Integer> {
    public CustomUserDetails findByUsername(String username);
}
