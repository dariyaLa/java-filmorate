package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class Genre {

    @Getter
    private int id;
    @Getter
    private String name;

}
