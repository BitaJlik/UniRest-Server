package com.unirest.core.repositories;

import com.unirest.core.models.Dormitory;
import com.unirest.core.models.Request;
import com.unirest.core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByDormitory(Dormitory dormitory);

    List<Request> findByUser(User user);
}
