package com.risefalcon.zasgateway.security_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class URL {
    public static final String ID = "id";
    public static final String MSID = "ms_id";
    public static final String PATH = "path";

    private String id;
    private String msId;
    private String path;

    public URL(String msId, String path) {
        this.id = UUID.randomUUID().toString();
        this.msId = msId;
        this.path = path;
    }
}
