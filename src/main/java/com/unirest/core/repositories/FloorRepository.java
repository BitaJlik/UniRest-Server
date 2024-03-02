package com.unirest.core.repositories;

import com.unirest.core.models.Dormitory;
import com.unirest.core.models.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    List<Floor> findByDormitoryId(Long id);
}
