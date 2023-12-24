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
public class UpdateUserController {

    private final String UPDATE_USER_TIMER_NAME = "updateUser.duration";
    private final String UPDATE_USER_TIMER_DESC = "Time taken to update user";

    private final String UPDATE_USER_ERROR_COUNTER_NAME = "updateUser.errorCounter";
    private final String UPDATE_USER_ERROR_COUNTER_DESC = "updateUser error counter";

    private final UserService userService;

    private final Timer updateUserTimer;
    private final Counter updateUserErrorCounter;

    @Autowired
    public UpdateUserController(UserService userService, MeterRegistry registry) {
        this.userService = userService;

        this.updateUserTimer = Timer.builder(UPDATE_USER_TIMER_NAME)
                .description(UPDATE_USER_TIMER_DESC)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        this.updateUserErrorCounter = Counter.builder(UPDATE_USER_ERROR_COUNTER_NAME)
                .description(UPDATE_USER_ERROR_COUNTER_DESC)
                .register(registry);
    }

    @PutMapping(path = "/user/{id}", consumes = "application/json")
    public void updateUser(@PathVariable Long id, @RequestBody User u) throws NotFoundException {
        Timer.Sample sample = Timer.start();
        try {
            User modifiedUser = userService.updateUser(id, u);
            log.info("User {} updated", modifiedUser);
        } finally {
            sample.stop(updateUserTimer);
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BusinessError handleNotFoundException(NotFoundException e) {
        updateUserErrorCounter.increment();
        return new BusinessError().setCode(BusinessError.NOT_FOUND_ERROR).setMessage(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessError handleException(Exception e) {
        updateUserErrorCounter.increment();
        return new BusinessError().setCode(BusinessError.GENERAL_ERROR).setMessage(e.getMessage());
    }




}
