package com.unirest.data.dto;

import com.unirest.data.models.Payment;
import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private long date;
    private double balance;
    private String checkId;

    private long moderateDate;
    private boolean moderated;
    private boolean valid;

    private Long dormitoryId;

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.date = payment.getDate();
        this.balance = payment.getBalance();
        this.checkId = payment.getCheckId();
        this.moderated = payment.isModerated();
        this.valid = payment.isValid();
        this.moderateDate = payment.getModerateDate();
        this.dormitoryId = payment.getDormitory().getId();
    }

}
