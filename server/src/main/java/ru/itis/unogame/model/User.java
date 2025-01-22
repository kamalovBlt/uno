package ru.itis.unogame.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private int id;
    private String userName;

}
