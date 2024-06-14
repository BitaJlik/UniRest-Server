package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.repositories.RequestTemplateRepository;
import com.unirest.core.services.PDFService;
import com.unirest.data.dto.RequestTemplateDTO;
import com.unirest.data.models.Dormitory;
import com.unirest.data.models.Request;
import com.unirest.data.dto.RequestDTO;
import com.unirest.core.repositories.RequestRepository;
import com.unirest.data.models.RequestTemplate;
import com.unirest.data.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/request")
public class RequestController extends BaseController<Request, Long, RequestDTO, RequestRepository> {

    private final RequestTemplateRepository templateRepository;
    private final PDFService pdfService;

    public RequestController(RequestRepository repository, RequestTemplateRepository templateRepository, PDFService service) {
        super(repository, Request.class, RequestDTO.class);
        this.templateRepository = templateRepository;
        this.pdfService = service;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getRequests(@RequestParam("id") Long dormitoryId) {
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

    @GetMapping("/of")
    public ResponseEntity<?> getUserRequests(@RequestParam("id") Long userId) {
        List<Request> allByDormitoryId = repository.findByUserId(userId);
        if (allByDormitoryId != null && !allByDormitoryId.isEmpty()) {
            List<RequestDTO> requestDTOs = new ArrayList<>();
            for (Request request : allByDormitoryId) {
                requestDTOs.add(wrapToDTO(request));
            }
            return ResponseEntity.ok(requestDTOs);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRequests(@RequestParam("id") Long dormitoryId) {
        List<RequestTemplate> allByDormitoryId = templateRepository.findAllByDormitoryId(dormitoryId);
        if (!allByDormitoryId.isEmpty()) {
            List<RequestTemplateDTO> list = new ArrayList<>();
            for (RequestTemplate requestTemplate : allByDormitoryId) {
                list.add(wrapToDTO(requestTemplate, RequestTemplateDTO.class));
            }
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/put")
    public ResponseEntity<?> putRequest(@RequestParam("id") Long dormitoryId, @RequestParam("userId") Long userId, @RequestParam("templateId") Long templateId) {
        if (dormitoryId != null && userId != null && templateId != null) {
            Request request = new Request();
            Dormitory dormitory = new Dormitory();
            dormitory.setId(dormitoryId);
            request.setDormitory(dormitory);
            User user = new User();
            user.setId(userId);
            request.setUser(user);
            request.setDate(System.currentTimeMillis());
            RequestTemplate template = new RequestTemplate();
            template.setId(templateId);
            request.setTemplate(template);
            repository.save(request);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/keys")
    public ResponseEntity<?> getKeys(@RequestParam("id") Long templateId) {
        Optional<RequestTemplate> templateOptional = templateRepository.findById(templateId);
        if (templateOptional.isPresent()) {
            RequestTemplate requestTemplate = templateOptional.get();
            return ResponseEntity.ok(requestTemplate.getKeysJson());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateRequest(@RequestParam("id") Long requestId, @RequestBody HashMap<String, String> replacements) {
        Optional<Request> byId = repository.findById(requestId);
        if (byId.isPresent()) {
            String string = UUID.randomUUID().toString().replace("-", "");
            Request request = byId.get();
            try {
                File fromTemplatePDF = pdfService.createFromTemplatePDF(request.getTemplate().getTemplateName(), replacements, string);
                request.setFileId(string);
                return ResponseEntity.ok(string);
            } catch (IOException e) {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/get/{request}")
    public ResponseEntity<?> getFileRequest(@PathVariable("request") String requestId) {
        if (requestId != null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfService.getPDFBytes(requestId));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/new")
    public ResponseEntity<?> registerNewRequest(@RequestParam("id") Long dormitoryId, @RequestBody RequestRegister register) {
        if (register != null) {
            RequestTemplate template = new RequestTemplate();
            Dormitory dormitory = new Dormitory();
            dormitory.setId(dormitoryId);
            template.setDormitory(dormitory);
            template.setName(register.getName());
            template.setTemplateName(register.getTemplateName());
            template.setLevel(register.getLevel());
            template.setRegisterTime(System.currentTimeMillis());
            template.setKeysJson(register.getKeysJson());
            templateRepository.save(template);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Getter
    @Setter
    public static class RequestRegister {
        private int level;
        private String keysJson;
        private String name;
        private String templateName;
    }

}
