package ru.itis.unogame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import ru.itis.request.Request;
import ru.itis.response.Response;
import ru.itis.service.MessageService;
import ru.itis.unogame.model.Game;
import ru.itis.unogame.model.GamePlayer;
import ru.itis.unogame.repository.GameRepository;
import ru.itis.unogame.util.LoopedList;

import java.util.LinkedList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final MessageService<Response, Request> serverProtocolService;

    @Override
    public Optional<Game> findGameById(int id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game createGame(GamePlayer gamePlayer) {
        LoopedList<GamePlayer> loopedList = getLoopedList();
        loopedList.add(gamePlayer);
        int id = gameRepository.getNextId();
        Game game = new Game(id, loopedList, new LinkedList<>(), serverProtocolService);
        gameRepository.save(game);
        return game;
    }

    @Lookup
    public LoopedList<GamePlayer> getLoopedList() {
        return null;
    }
}
