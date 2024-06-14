package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.Payment;
import com.unirest.data.models.User;
import com.unirest.data.dto.PaymentDTO;
import com.unirest.core.repositories.PaymentRepository;
import com.unirest.core.repositories.UserRepository;
import com.unirest.core.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController<Payment, Long, PaymentDTO, PaymentRepository> {

    private final ImageService imageService;
    private final UserRepository userRepository;

    @Autowired
    public PaymentController(PaymentRepository repository, UserRepository userRepository, ImageService imageService) {
        super(repository, Payment.class, PaymentDTO.class);
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public ResponseEntity<?> add(Payment payment) {
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> add(@RequestParam("id") Long userId, @RequestBody Payment payment) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();
            if (payment != null) {
                String checkId = UUID.randomUUID().toString();
                payment.setCheckId(checkId);
                payment.setUser(user);
                payment.setDormitory(user.getDormitory());
                repository.save(payment);
                return ResponseEntity.ok(checkId);
            }
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPayments(@RequestParam("id") Long userId) {
        List<Payment> payments = repository.findAllByUserId(userId);
        if (payments != null && !payments.isEmpty()) {
            List<PaymentDTO> paymentDTOS = new ArrayList<>();
            for (Payment payment : payments) {
                paymentDTOS.add(new PaymentDTO(payment));
            }
            return ResponseEntity.ok(paymentDTOS);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/moderate/list")
    public ResponseEntity<?> getPaymentsDormitory(@RequestParam("id") Long dormitoryId) {
        List<Payment> payments = repository.findAllByDormitoryId(dormitoryId);
        if (payments != null && !payments.isEmpty()) {
            List<PaymentDTO> paymentDTOS = new ArrayList<>();
            for (Payment payment : payments) {
                paymentDTOS.add(new PaymentDTO(payment));
            }
            return ResponseEntity.ok(paymentDTOS);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/moderate")
    public ResponseEntity<?> moderatePayment(@RequestParam("id") Long paymentId, @RequestParam("valid") boolean isValid) {
        Optional<Payment> byId = repository.findById(paymentId);
        if (byId.isPresent()) {
            Payment payment = byId.get();
            if (payment.isModerated()) {
                if (payment.isValid() && !isValid) {
                    payment.setValid(false);
                    User user = payment.getUser();
                    user.setBalance(user.getBalance() - payment.getBalance());
                    repository.save(payment);
                    return ResponseEntity.ok().build();
                } else if (!payment.isValid() && isValid) {
                    payment.setValid(true);
                    User user = payment.getUser();
                    user.setBalance(user.getBalance() + payment.getBalance());
                    repository.save(payment);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.badRequest().build();
            } else {
                payment.setModerated(true);
                payment.setValid(isValid);
                payment.setModerateDate(System.currentTimeMillis());
                if (isValid) {
                    User user = payment.getUser();
                    user.setBalance(user.getBalance() + payment.getBalance());
                }
                repository.save(payment);
                return ResponseEntity.status(202).build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/check/upload")
    public ResponseEntity<?> uploadCheck(@RequestParam("id") String checkId, @RequestPart("image") MultipartFile multipartFile) {
        imageService.saveImage("checks", checkId, multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<?> getCheck(@PathVariable("id") String checkId) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.getImageById("checks", checkId));
    }

}
