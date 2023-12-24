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
public class DeleteUserController {

    private final String DELETE_USER_TIMER_NAME = "deleteUser.duration";
    private final String DELETE_USER_TIMER_DESC = "Time taken to delete user";

    private final String DELETE_USER_ERROR_COUNTER_NAME = "deleteUser.errorCounter";
    private final String DELETE_USER_ERROR_COUNTER_DESC = "deleteUser error counter";

    private final UserService userService;

    private final Timer deleteUserTimer;
    private final Counter deleteUserErrorCounter;


    @Autowired
    public DeleteUserController(UserService userService, MeterRegistry registry) {
        this.userService = userService;

        this.deleteUserTimer = Timer.builder(DELETE_USER_TIMER_NAME)
                .description(DELETE_USER_TIMER_DESC)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        this.deleteUserErrorCounter = Counter.builder(DELETE_USER_ERROR_COUNTER_NAME)
                .description(DELETE_USER_ERROR_COUNTER_DESC)
                .register(registry);
    }

    @DeleteMapping(path = "/user/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) throws NotFoundException {
        Timer.Sample sample = Timer.start();
        try {
            User deletedUser = userService.deleteUser(id);
            log.info("User {} deleted", deletedUser);
        } finally {
            sample.stop(deleteUserTimer);
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BusinessError handleNotFoundException(NotFoundException e) {
        deleteUserErrorCounter.increment();
        return new BusinessError().setCode(BusinessError.NOT_FOUND_ERROR).setMessage(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BusinessError handleException(Exception e) {
        deleteUserErrorCounter.increment();
        return new BusinessError().setCode(BusinessError.GENERAL_ERROR).setMessage(e.getMessage());
    }


}
