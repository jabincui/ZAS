package com.risefalcon.zasgateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class URL {
    private String id;
    private String msId;
    private String path;

    public URL(String msId, String path) {
        this.msId = msId;
        this.path = path;
    }
}
