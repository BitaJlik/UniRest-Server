package com.unirest.core.repositories;

import com.unirest.data.models.Washer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasherRepository extends JpaRepository<Washer, Long> {
    List<Washer> findAllByFloorId(Long id);
}
