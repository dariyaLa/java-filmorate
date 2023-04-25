package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class User {

    private static int countId = 0;

    private int id;
    @Email(message = "Email not valid")
    private String email;
    @NotBlank(message = "Login not valid")
    private String login;
    private String name;
    @Past(message = "Birthday not valid")
    private LocalDate birthday;
    private HashSet<String> friends;

    public static int generateId() {
        return ++countId;
    }

}