package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.models.Floor;
import com.unirest.core.models.dto.DTOFloor;
import com.unirest.core.repositories.FloorRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/floor")
public class FloorController extends BaseController<Floor, Long, DTOFloor, FloorRepository> {

    public FloorController(FloorRepository repository) {
        super(repository, Floor.class, DTOFloor.class);
    }



}
