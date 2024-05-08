package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.Request;
import com.unirest.data.dto.RequestDTO;
import com.unirest.core.repositories.RequestRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RequestController extends BaseController<Request, Long, RequestDTO, RequestRepository> {

    public RequestController(RequestRepository repository) {
        super(repository, Request.class, RequestDTO.class);
    }

}
