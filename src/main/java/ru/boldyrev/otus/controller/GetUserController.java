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
public class GetUserController {

    private final String GET_USER_TIMER_NAME = "getUser.duration";
    private final String GET_USER_TIMER_DESC = "Time taken to get user";

    private final String GET_USER_ERROR_COUNTER_NAME = "getUser.errorCounter";
    private final String GET_USER_ERROR_COUNTER_DESC = "getUser error counter";

    private final UserService userService;

    private final Timer getUserTimer;
    private final Counter getUserErrorCounter;

    @Autowired
    public GetUserController(UserService userService, MeterRegistry registry) {
        this.userService = userService;

        this.getUserTimer = Timer.builder(GET_USER_TIMER_NAME)
                .description(GET_USER_TIMER_DESC)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        this.getUserErrorCounter = Counter.builder(GET_USER_ERROR_COUNTER_NAME)
                .description(GET_USER_ERROR_COUNTER_DESC)
                .register(registry);
    }

    @GetMapping(path = "/user/{id}", produces = "application/json")
    public User getUser(@PathVariable Long id) throws NotFoundException {
        Timer.Sample sample = Timer.start();
        try {
            return userService.getUserById(id);
        } finally {
            sample.stop(getUserTimer);
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BusinessError handleNotFoundException(NotFoundException e) {
        getUserErrorCounter.increment();
        return new BusinessError().setCode(BusinessError.NOT_FOUND_ERROR).setMessage(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessError handleException(Exception e) {
        getUserErrorCounter.increment();
        return new BusinessError().setCode(BusinessError.GENERAL_ERROR).setMessage(e.getMessage());
    }






}
