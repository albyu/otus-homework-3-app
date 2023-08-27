package ru.boldyrev.otus.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.boldyrev.otus.model.HealthResponse;
import ru.boldyrev.otus.model.StatusEnum;
import ru.boldyrev.otus.model.VersionResponse;

@RestController
@RequestMapping("/")
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public HealthResponse homework1Controller() {
        return new HealthResponse(StatusEnum.OK);
    }

    @GetMapping("/version")
    public VersionResponse versionController() {
        return new VersionResponse("v2.0");
    }
}
