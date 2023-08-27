package ru.boldyrev.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.boldyrev.otus.model.User;

public interface UserRepo extends JpaRepository<User, Long> { }
