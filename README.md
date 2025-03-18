# 2-Player-Chess

## Overview
This project is a command-line implementation of a two-player chess game written in Java. It demonstrates strong object-oriented design principles, including encapsulation, inheritance, and polymorphism. The game supports legal chess moves, special move handling, and game termination conditions.

## Features
- **Text-based UI**: Displays the chessboard in the terminal.
- **Turn-based play**: Players take alternating turns.
- **Move validation**: Ensures only legal chess moves are made.
- **Special moves**: Supports castling, en passant, and pawn promotion.
- **Game termination**: Recognizes checkmate, resignation, and draw conditions.
- **Rule enforcement**: Prevents illegal moves, including self-check scenarios.

## How to Run
1. Place all .java files inside a folder named chess
2. Compile the program:
   ```sh
   javac chess/*.java
   ```
3. Run the game using the provided `PlayChess.java` application:
   ```sh
   java chess.PlayChess
   ```

## How to Play
- The game starts with White's move.
- Moves must be formatted in algebraic notation (e.g., `e2 e4`).
- **Special Move Formats:**
  - **Resignation**: A player can resign by typing `resign`, immediately ending the game.
  - **Draw Request**: A draw can be initiated by appending `draw?` to a move (e.g., `e2 e4 draw?`).
  - **Pawn Promotion**: A pawn reaching the last rank can be promoted (e.g., `g7 g8 N` for a knight, `g7 g8 Q` for a queen).
  - **Castling**: Indicated by moving the king (e.g., `e1 g1` for kingside castling).
- Any illegal move results in the same player being prompted again until a valid move is made.

## Project Structure
- **Chess.java**: Core game logic, managing board state and player moves. Contains the start() method to initialize the game and play(String move) to process moves.
- **Board.java**: Manages the board state, move validation, and check/checkmate conditions.
- **Piece Classes**: Individual classes for each piece (Pawn, Rook, Knight, Bishop, Queen, King) encapsulate movement rules.
- **Move Handling**: Implements validation for moves, special conditions, and game-ending states. Prevents illegal moves, including moving into check.
- **Return Objects**: Uses `ReturnPlay` and `ReturnPiece` to maintain board integrity. The play() method returns a ReturnPlay object with the updated board state.

## Demonstrated Skills
- **Object-Oriented Programming (OOP)**: Implements polymorphism, encapsulation, and inheritance for piece behaviors.
- **Algorithmic Thinking**: Efficiently validates chess moves, implements game rules, and handles edge cases.
- **Java Proficiency**: Follows Java best practices with structured package management and clean code architecture.
- **Data Structures**: Utilizes 2D arrays to represent the board state and HashMaps for piece mapping.

## Future Improvements
- Implement GUI for a more interactive experience.
- Add AI opponent for single-player mode.

