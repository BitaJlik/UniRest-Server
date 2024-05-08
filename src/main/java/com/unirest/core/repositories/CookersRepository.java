package com.unirest.core.repositories;

import com.unirest.data.models.Cooker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookersRepository extends JpaRepository<Cooker, Long> {
}
