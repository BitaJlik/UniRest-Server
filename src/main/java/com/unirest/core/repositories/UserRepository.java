package com.unirest.core.repositories;

import com.unirest.data.models.Dormitory;
import com.unirest.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByRoomId(Long room);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:keyword% OR u.lastName LIKE %:keyword%")
    List<User> searchAllByKeyword(@Param("keyword") String keyword);

    List<User> searchAllByRoomIsNull();

}
