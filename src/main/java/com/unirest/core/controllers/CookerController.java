package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.dto.CookerDTO;
import com.unirest.data.models.Cooker;
import com.unirest.core.repositories.CookersRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cooker")
public class CookerController extends BaseController<Cooker, Long, CookerDTO, CookersRepository> {
    public CookerController(CookersRepository repository) {
        super(repository, Cooker.class, CookerDTO.class);
    }
}
