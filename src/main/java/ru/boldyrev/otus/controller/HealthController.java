package ru.boldyrev.otus.controller;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.boldyrev.otus.metrics.RequestCounter;
import ru.boldyrev.otus.model.HealthResponse;
import ru.boldyrev.otus.model.StatusEnum;
import ru.boldyrev.otus.model.VersionResponse;

@RestController
@RequestMapping("/")
@Slf4j
public class HealthController {
    private final RequestCounter requestCounter;

    @Autowired
    public HealthController(RequestCounter requestCounter){
        this.requestCounter = requestCounter;
    }

    @GetMapping("/health")
    public HealthResponse homework1Controller() {
        return new HealthResponse(StatusEnum.OK);
    }

    @GetMapping("/version")
    @Timed(value = "request_version_time", description = "Time taken to return version")
    public VersionResponse versionController() {
        requestCounter.increment();
        return new VersionResponse("v4.0");
    }
}
