package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.models.Dormitory;
import com.unirest.core.models.dto.DTODormitory;
import com.unirest.core.repositories.DormitoryRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dormitory")
public class DormitoryController extends BaseController<Dormitory, Long, DTODormitory, DormitoryRepository> {


    public DormitoryController(DormitoryRepository dormitoryRepository) {
        super(dormitoryRepository, Dormitory.class, DTODormitory.class);
    }

}
