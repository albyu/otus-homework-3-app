package ru.boldyrev.otus.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.boldyrev.otus.exception.NotFoundException;
import ru.boldyrev.otus.model.BusinessError;
import ru.boldyrev.otus.model.User;
import ru.boldyrev.otus.service.UserService;

@RestController
@RequestMapping("/")
@Slf4j
public class CreateUserController {
    private final String CREATE_USER_TIMER_NAME = "createUser.duration";
    private final String CREATE_USER_TIMER_DESC = "Time taken to create user";

    private final String CREATE_USER_ERROR_COUNTER_NAME = "createUser.errorCounter";
    private final String CREATE_USER_ERROR_COUNTER_DESC = "createUser error counter";


    private final UserService userService;

    private final Timer createUserTimer;
    private final Counter createUserErrorCounter;

    @Autowired
    public CreateUserController(UserService userService, MeterRegistry registry) {
        this.userService = userService;

        this.createUserTimer = Timer.builder(CREATE_USER_TIMER_NAME)
                .description(CREATE_USER_TIMER_DESC)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        this.createUserErrorCounter = Counter.builder(CREATE_USER_ERROR_COUNTER_NAME)
                .description(CREATE_USER_ERROR_COUNTER_DESC)
                .register(registry);

    }

    @PostMapping(path = "/user", consumes = "application/json")
    public void createUser(@RequestBody User u) {
        Timer.Sample sample = Timer.start();
        try {
            User newUser = userService.createUser(u);
            log.info("User {} created", newUser.toString());
        } finally {
            sample.stop(createUserTimer);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessError handleException(Exception e) {
        createUserErrorCounter.increment();
        return new BusinessError().setCode(BusinessError.GENERAL_ERROR).setMessage(e.getMessage());
    }



}
