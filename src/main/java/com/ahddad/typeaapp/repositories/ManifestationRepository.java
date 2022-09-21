package com.ahddad.typeaapp.repositories;

import com.ahddad.typeaapp.models.Manifestation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManifestationRepository extends JpaRepository<Manifestation , Integer> {
    public List<Manifestation> findAllByUsername(String username);
    public Long deleteById(int id);
}
