package ru.boldyrev.otus.controller;

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
public class CrudController {

  @Autowired
  UserService userService;


  @GetMapping(path = "/user/{id}", produces = "application/json")
  public User getUser(@PathVariable Long id) throws NotFoundException {
     return userService.getUserById(id);
  }

  @PutMapping(path = "/user/{id}", consumes = "application/json")
    public void putUser (@PathVariable Long id, @RequestBody User u) throws NotFoundException {
    User modifiedUser = userService.updateUser(id, u);

    log.info("User {} updated", modifiedUser);
  }

  @PostMapping(path = "/user", consumes = "application/json")
  public void postUser (@RequestBody User u){
    User newUser = userService.createUser(u);

    log.info("User {} created", newUser.toString());
  }

  @DeleteMapping(path = "/user/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteMapping(@PathVariable Long id) throws NotFoundException {
    User deletedUser = userService.deleteUser(id);

    log.info("User {} deleted", deletedUser);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public BusinessError handleNotFoundException(NotFoundException e){
    return new BusinessError().setCode(BusinessError.NOT_FOUND_ERROR).setMessage(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BusinessError handleException(Exception e){
    return new BusinessError().setCode(BusinessError.GENERAL_ERROR).setMessage(e.getMessage());
  }


}
