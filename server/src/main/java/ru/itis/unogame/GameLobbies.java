package ru.itis.unogame;

import java.util.ArrayList;
import java.util.List;

public class GameLobbies {

    public static List<GameLobby> gameLobbies = new ArrayList<GameLobby>();

    public static void addGameLobby(GameLobby gameLobby) {
        gameLobbies.add(gameLobby);
    }

    public static void removeGameLobby(GameLobby gameLobby) {
        gameLobbies.remove(gameLobby);
    }

    public static GameLobby getGameLobby(int id) {
        for (GameLobby gameLobby : gameLobbies) {
            if (gameLobby.getId() == id) {
                return gameLobby;
            }
        }
        return null;
    }

}
