package com.unirest.core.controllers;

import com.unirest.core.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/server")
public class MainController {
    @Autowired
    private FileService fileService;

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/error", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void receiveError(@RequestBody String jsonError) {
        fileService.saveFile("errors", UUID.randomUUID().toString(), jsonError);
    }

}
