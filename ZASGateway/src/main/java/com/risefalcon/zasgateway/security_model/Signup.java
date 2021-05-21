package com.risefalcon.zasgateway.security_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signup {
    private String id;
    private String name;
    private List<String> rolesId;

    public Signup(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.rolesId = new ArrayList<>();
    }
}
