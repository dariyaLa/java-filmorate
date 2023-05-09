package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Mpa {

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String name;

    public Mpa(int id) {
        this.id = id;
    }

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
