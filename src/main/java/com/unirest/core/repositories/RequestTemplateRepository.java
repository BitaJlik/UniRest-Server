package com.unirest.core.repositories;

import com.unirest.data.models.RequestTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestTemplateRepository extends JpaRepository<RequestTemplate, Long> {
    List<RequestTemplate> findAllByDormitoryId(Long id);
}
