package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.models.Payment;
import com.unirest.core.repositories.PaymentRepository;
import com.unirest.core.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController<Payment, Long, Payment, PaymentRepository> {

    @Autowired
    private ImageService imageService;

    public PaymentController(PaymentRepository repository) {
        super(repository, Payment.class, Payment.class);
    }

    @Override
    public ResponseEntity<?> add(@RequestBody Payment payment) {
        return ResponseEntity.status(503).build();
    }

    @PostMapping("/check/upload")
    public ResponseEntity<?> uploadCheck(@RequestParam("id") String checkId, @RequestPart("image") MultipartFile multipartFile) {
        imageService.saveImage("checks", checkId, multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<?> getCheck(@PathVariable("id") String checkId) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageService.getImageById("checks", checkId));
    }

}
