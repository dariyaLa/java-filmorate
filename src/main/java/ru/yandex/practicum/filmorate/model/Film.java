package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.DataConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    @Getter
    private Mpa mpa;

    private List<Genre> genres;

    public static int generateId() {
        return ++countId;
    }

    public Collection<Genre> getGenres() {
        if (genres == null) {
            return new ArrayList<>();
        }
        return genres;
    }
}