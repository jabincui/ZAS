package com.risefalcon.zasgateway.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flow {
    private String id;
    private String userId;
    private String url;
    private String date;
}
