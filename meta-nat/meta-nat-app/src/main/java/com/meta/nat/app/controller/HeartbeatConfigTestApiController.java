package com.meta.nat.app.controller;

import com.meta.nat.app.config.NatConfig;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HeartbeatConfigTestApiController {

    private final NatConfig natConfig;

    @GetMapping
    public String save() {
        return natConfig.toString();
    }

}
