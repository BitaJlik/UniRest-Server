package com.unirest.core.repositories;

import com.unirest.core.models.Dormitory;
import com.unirest.core.models.Floor;
import com.unirest.core.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByFloorId(Long floor);
}
