package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.DataConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private static int countId = 0;

    private int id;
    @NotBlank(message = "Name not valid")
    private String name;
    @Size(max = 200, message = "Description not valid")
    private String description;
    @DataConstraint(message = "ReleaseDate not valid")
    private LocalDate releaseDate;
    @Min(0)
    private long duration;
    private HashSet<String> likes;

    public static int generateId() {
        return ++countId;
    }

}