package ru.itis.unogame.repository;

import ru.itis.unogame.model.Game;

import java.util.Optional;

public interface GameRepository {
    int getNextId();
    void save(Game game);
    Optional<Game> findById(int id);
}
