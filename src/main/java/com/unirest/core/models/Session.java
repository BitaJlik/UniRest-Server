package com.unirest.core.models;

import com.unirest.core.utils.IProviderId;
import com.unirest.core.utils.IToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.util.HashMap;


@Getter
@Setter
@Entity
@Table(name = "sessions")
public class Session implements IToken, IProviderId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int current = 100;
    private int max = 200;

    private long lastUpdate = -1;
    private long expire = System.currentTimeMillis() + Duration.ofDays(15).toMillis();

    public void updateDate() {
        long currentTime = System.currentTimeMillis();
        if (lastUpdate != -1 && currentTime > lastUpdate) {
            int tokens = (int) (currentTime - lastUpdate / 30_000);
            current = Math.min(current + tokens, max);
        }
        lastUpdate = currentTime;
    }

    @Override
    public String getSubject() {
        return null;
    }

    @Override
    public HashMap<String, ?> getValues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("session_id", getId());
        map.put("exp", getExpire());
        return map;
    }
}
