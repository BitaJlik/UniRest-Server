package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.security.ValidateToken;
import com.unirest.data.models.Request;
import com.unirest.data.dto.RequestDTO;
import com.unirest.core.repositories.RequestRepository;
import com.unirest.data.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController extends BaseController<Request, Long, RequestDTO, RequestRepository> {

    public RequestController(RequestRepository repository) {
        super(repository, Request.class, RequestDTO.class);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getRequests(Long dormitoryId) {
        List<Request> allByDormitoryId = repository.findAllByDormitoryId(dormitoryId);
        if (allByDormitoryId != null && !allByDormitoryId.isEmpty()) {
            List<RequestDTO> requestDTOs = new ArrayList<>();
            for (Request request : allByDormitoryId) {
                requestDTOs.add(wrapToDTO(request));
            }
            return ResponseEntity.ok(requestDTOs);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/put")
    public ResponseEntity<?> putRequest(Long dormitoryId, RequestDTO requestDTO) {
        Request request = new Request();
        repository.save(request);
        return ResponseEntity.badRequest().build();
    }

}
