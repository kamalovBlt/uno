package ru.itis.lobby;

/**<p>Сервер отправляет кажддому участнику данный Lobby</p>
 * Важно соблюдать порядок, иначе все полетит*/
public record Lobby(Player leftPlayer, Player topPlayer, Player rightPlayer) {}
