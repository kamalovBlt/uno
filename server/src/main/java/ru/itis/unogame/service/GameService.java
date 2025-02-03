package ru.itis.unogame.service;

import ru.itis.unogame.model.Game;
import ru.itis.unogame.model.GamePlayer;

import java.util.Optional;

public interface GameService {
    Optional<Game> findGameById(int id);
    Game createGame(GamePlayer gamePlayer);
}
