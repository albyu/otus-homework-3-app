package ru.boldyrev.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.boldyrev.otus.exception.NotFoundException;
import ru.boldyrev.otus.model.User;
import ru.boldyrev.otus.repo.UserRepo;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public User getUserById(Long id) throws NotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found");
        }

    }

    public User createUser(User user) {
        User newUser = userRepo.save(user);
        return newUser;

    }

    public User updateUser(Long id, User userDetails) throws NotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();

            if (userDetails.getUsername() != null)
                existingUser.setUsername(userDetails.getUsername());

            if (userDetails.getFirstName() != null)
                existingUser.setFirstName(userDetails.getFirstName());

            if (userDetails.getFirstName() != null)
                existingUser.setFirstName(userDetails.getFirstName());

            if (userDetails.getLastName() != null)
                existingUser.setLastName(userDetails.getLastName());

            if (userDetails.getEmail() != null)
                existingUser.setLastName(userDetails.getEmail());

            if (userDetails.getPhone() != null)
                existingUser.setLastName(userDetails.getLastName());


            existingUser.setEmail(userDetails.getEmail());
            return userRepo.save(existingUser);
        } else
            throw new NotFoundException("User not found");
    }

    public User deleteUser(Long id) throws NotFoundException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.deleteById(id);
            return null;
        } else
            throw new NotFoundException("User not found");
    }

}
