package com.unirest.core.repositories;

import com.unirest.data.models.Request;
import com.unirest.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByDormitoryId(Long id);

    List<Request> findByUser(User user);
}
