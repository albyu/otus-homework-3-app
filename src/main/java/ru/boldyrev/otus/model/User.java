package ru.boldyrev.otus.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Override
    public String toString(){
      return new StringBuilder()
              .append("id = ").append(id)
              .append(", username = ").append(username)
              .append(", firstName = ").append(firstName)
              .append(", lastName = ").append(lastName)
              .append(", email = ").append(email)
              .append(", phone = ").append(phone).toString();
    }
}
