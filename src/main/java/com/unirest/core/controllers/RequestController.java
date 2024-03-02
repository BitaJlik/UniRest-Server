package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.models.Request;
import com.unirest.core.models.dto.DTORequest;
import com.unirest.core.repositories.RequestRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RequestController extends BaseController<Request, Long, DTORequest, RequestRepository> {

    public RequestController(RequestRepository repository) {
        super(repository, Request.class, DTORequest.class);
    }

}
