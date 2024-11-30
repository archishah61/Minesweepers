# Minesweepers
# Overview of the Minesweeper Game Project
The Minesweeper Game is a console-based implementation of the classic puzzle game, developed in Java. This project serves as an educational exercise in utilizing data structures effectively, particularly arrays and control structures, to create an interactive gaming experience.

# Game Structure:
The game consists of a grid where players must uncover squares while avoiding hidden mines.
Players can choose from three difficulty levels:
Easy: 5x5 grid with 10 mines
Medium: 8x8 grid with 20 mines
Hard: 10x10 grid with 35 mines

# Core Methods:
chooseDifficultyLevel(): Allows users to select their desired difficulty level.
createBoard(): Initializes the game board and a separate system board for mine placement.
putMines(): Randomly places mines on the system board.
replaceMine(): Ensures that the first move does not hit a mine by replacing it with a safe square.
countMines(): Counts the number of mines surrounding a given square.
isValid(): Validates whether a player's move is within the bounds of the board.
isMine(): Checks if a specific cell contains a mine.
giveHint(): Provides information about whether there is a mine at specified coordinates.
printBoard(): Displays the current state of the playing board.

# Gameplay Mechanics:
Players make moves by entering coordinates to uncover squares.
The game tracks remaining moves and allows players to use hints and redo moves within certain limits.
The game ends when all safe squares are revealed or when a mine is hit.
Implementation Details
The game utilizes a two-dimensional character array for both the system board (which contains mines) and the play board (which players interact with).
A random number generator is used to place mines on the system board, ensuring varied gameplay experiences each time the game is played.
The program includes user-friendly prompts and error handling to enhance player interaction.

# Conclusion
This Minesweeper implementation not only provides an engaging gaming experience but also serves as a practical application of data structures in Java. It demonstrates how to manage game states, handle user input, and implement game logic effectively.
