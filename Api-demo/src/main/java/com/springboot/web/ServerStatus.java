package com.springboot.web;
import lombok.Data;

@Data
public class ServerStatus {
    private Long id;
    private String name;
    private String status;
    private String cpu;
    private String memory;
    private String location;
    private String xmlStr;

    public ServerStatus(Long id,
                  String name,
                  String status,
                  String cpu,
                  String memory,
                  String location) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.cpu = cpu;
        this.memory = memory;
        this.location = location;
    }
}
