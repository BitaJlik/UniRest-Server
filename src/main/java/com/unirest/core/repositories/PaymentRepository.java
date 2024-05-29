package com.unirest.core.repositories;

import com.unirest.data.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByUserId(Long id);
    List<Payment> findAllByDormitoryId(Long id);
}
