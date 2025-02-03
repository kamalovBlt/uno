package ru.itis.unogame.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.itis.unogame.model.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameRepositoryImpl implements GameRepository {

    private final Map<Integer, Game> gameMap = new HashMap<>();
    private int nextId = 1;

    @Override
    public int getNextId() {
        return nextId++;
    }

    @Override
    public void save(Game game) {
        gameMap.put(game.getId(), game);
    }

    @Override
    public Optional<Game> findById(int id) {
        return Optional.ofNullable(gameMap.get(id));
    }
}
