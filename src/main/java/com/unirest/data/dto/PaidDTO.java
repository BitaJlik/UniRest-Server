package com.unirest.data.dto;

import com.unirest.data.models.Paid;
import lombok.Data;

@Data
public class PaidDTO {
    private Long id;
    private long date;
    private double balance;

    private Long dormitoryId;

    public PaidDTO() {
    }

    public PaidDTO(Paid paid) {
        this.id = paid.getId();
        this.date = paid.getDate();
        this.balance = paid.getBalance();
        this.dormitoryId = paid.getDormitory().getId();
    }

}
