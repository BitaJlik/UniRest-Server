package com.unirest.core.repositories;

import com.unirest.data.models.Cooker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookersRepository extends JpaRepository<Cooker, Long> {
    List<Cooker> findAllByFloorId(Long floorId);
}
