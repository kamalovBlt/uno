# Play UNO with AI opponents or friends

- Fully implemented game rules
- Interactive graphical user interface
- Support for special cards (Skip, Reverse, Draw Two, Wild, Wild Draw Four)
- Turn-based gameplay

## Technologies Used

- Java
- JavaFX

## Installation and Running

### Prerequisites

- Java 17 or higher installed
- JavaFX SDK (if required)

### Running the Game

1. Clone the repository:
   ```sh
   git clone <repository_url>
   cd uno-game
   ```
2. Start Server
   ```sh
   java Server.java
   ```
2. Compile and run the application:
   ```sh
   mvn javafx:run
   ```

## How to Play

1. Start the game and choose the number of players.
2. Each player receives 7 cards.
3. Play a card that matches the color or number of the top card in the discard pile.
4. Use action cards strategically to disrupt opponents.
5. If you have one card left, shout "UNO!" before your turn ends.
6. The first player to run out of cards wins!

## Future Improvements

- Multiplayer mode (online/local)
- Improved AI for smarter gameplay
- Additional UNO variants and custom rules

## License

This project is licensed under the MIT License.

