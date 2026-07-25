package com.springboot.service;

import com.springboot.web.ServerStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    public List<ServerStatus> findAll() {
        List<ServerStatus> servers = new ArrayList<>();
        servers.add(new ServerStatus(
                1L,
                "Server A",
                "Running",
                "20%",
                "50%",
                "Toronto"));

        servers.add(new ServerStatus(
                2L,
                "Server B",
                "Stopped",
                "0%",
                "10%",
                "Montreal"));

        servers.add(new ServerStatus(
                3L,
                "Server C",
                "Running",
                "65%",
                "80%",
                "Vancouver"));

        return servers;
    }

    public ServerStatus findById(Long id) {
        List<ServerStatus> servers = findAll();
        return servers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
