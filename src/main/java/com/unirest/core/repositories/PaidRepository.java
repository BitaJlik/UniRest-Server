package com.unirest.core.repositories;

import com.unirest.data.models.Paid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaidRepository extends JpaRepository<Paid, Long> {
}
