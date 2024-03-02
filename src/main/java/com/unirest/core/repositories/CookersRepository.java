package com.unirest.core.repositories;

import com.unirest.core.models.Cooker;
import com.unirest.core.models.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookersRepository extends JpaRepository<Cooker, Long> {
}
