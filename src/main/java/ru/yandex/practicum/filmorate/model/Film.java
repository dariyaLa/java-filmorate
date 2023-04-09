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
public class Film implements Comparable<Film> {

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

    @Override
    public int compareTo(Film o) {
        if (this.likes == null && o.getLikes() == null) {
            if (this.id < o.getId()) {
                return -1;
            }
            return 1;
        }
        if (this.likes.isEmpty() && o.getLikes() == null) {
            if (this.id < o.getId()) {
                return -1;
            }
            return 1;
        }
        if (this.likes == null && o.getLikes().isEmpty()) {
            if (this.id < o.getId()) {
                return -1;
            }
            return 1;
        }
        if (this.likes == null && !o.getLikes().isEmpty()) {
            return 1;
        }
        if (!this.likes.isEmpty() && o.getLikes() == null) {
            return -1;
        }
        return this.likes.size() - o.getLikes().size();
    }
}