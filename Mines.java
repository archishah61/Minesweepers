/*
TITLE : MINESWEEPER GAME 
DATE : 30-09-2023
SUBJECT : DATA STRUCTURE GROUP PROJECT
 */


/*
 *                                Some of the methods used in the program
 * 
 *  createBoard                    --> create system board and play board
 *  void chooseDifficultyLevel()   --> to choose the level
 *  void createBoard()             --> to create boards
 *  void putMines()                --> to randomly put mines
 *  void replaceMine()             --> to replace the mine if user encounters mine in the first move
 *  int countMines()               --> count mines around a particular coordinats
 *  boolean isValid()              --> to check whether the move is valid or not
 *  boolean isMine()               --> to check whether there is a mine or not
 *  void giveHint()                --> to give hint to the user
 *  void printBoard()              --> print the playing board
 * 
 */


import java.util.*;
class Minesweeper {

    static int side; // side length of the board
    static int mines; // number of minePos on the board
    static int move[];
    static int redoChance = 2;
    static int hintChance = 3;
    static char[][] systemBoard;
    static char[][] playBoard;
    static char[][] dummyBoard;
    static int[][] minePos;
    static int movesLeft[]=new int[1];
    static Scanner sc = new Scanner(System.in);

     static void chooseDifficultyLevel() {
        System.out.println("Enter to choose your level...");
        System.out.println("1.) Easy \n2.) Medium \n3.) Hard");
        int select=sc.nextInt();
        switch(select){
            case 1: side =5;
                    mines =10;
                    break;
            case 2: side =8;
                    mines =20;
                    break;
            case 3: side =10;
                    mines =35;
                    break;
            default: System.out.println("Please enter valid number");
                     chooseDifficultyLevel();
        }
    }

    // creates system board and play board --> 
    // playboard --> the board in which we are goin to play
    // system board --> the inbuilt board for comparing purpose

    static void createBoard(char[][] systemBoard, char[][] playBoard) {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                systemBoard[i][j] = '-';
                playBoard[i][j] = '-';
            }
        }
    }

    // to randomly generate mines within the systemBoard by using random method

    static void putMines(int[][] minePos, char[][] systemBoard) {
        boolean[] check = new boolean[side * side];
        for (int i=0;i<mines;) {
            int r = new Random().nextInt(side * side);
            int x = r/side;
            int y = r%side;

            if (!check[r]) {
                systemBoard[x][y] = '*';
                check[r] = true;
                i++;
            }
        }
    }

    // if in the first input - user encounters a mine - this method will replace mine with the nearest co-ordinate where mine is not present

    static void replaceMine(int row, int col, char[][] board) {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                if (board[i][j] != '*') {
                    board[i][j] = '*';
                    board[row][col] = '-';
                    return;
                }
            }
        }
    }

    static void startGame() {
        boolean gameOver = false;
        systemBoard = new char[side][side];
        playBoard = new char[side][side];
        minePos = new int[mines][2];
        dummyBoard = new char[side][side];
        move = new int[2];
        movesLeft[0]=(side*side)-mines;

        createBoard(systemBoard, playBoard);
        putMines(minePos, systemBoard);

        int currentMoveIndex = 0;
        while (!gameOver) {
            System.out.println("Current Status of Board : ");
            printBoard(playBoard);
            System.out.println("\nMoves Left:- "+movesLeft[0]);
            makeMove(move);

            if (currentMoveIndex == 0 && isMine(move[0], move[1], systemBoard)) {
                replaceMine(move[0], move[1], systemBoard);
                movesLeft[0]--;
            }
            currentMoveIndex++;

           // System.out.println("making dummy ready");
            for(int i=0;i<side;i++){
                for(int j=0;j<side;j++){
                    dummyBoard[i][j] = playBoard[i][j];
                }
            }

            gameOver = placeMinesCount(playBoard, systemBoard, minePos, move[0], move[1], movesLeft);
         
            if (!gameOver && movesLeft[0] == 0) {
                System.out.println("\nYou won !");
                gameOver = true;
            }
        }
    }

    // it counts the mine surrounding the h=given input. if the inout is at a position where there is no mine, the count of mine indicates the mine surrounding that input position in any of the other 8 direction

    static int countMines(int row, int col, int[][] minePos, char[][] realBoard) {
        int count = 0;
        int directions[][]={{0, 1},{1, 1},{ 1, 0},{-1, 1},{-1, 0},{-1, -1},{0, -1},{1, -1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValid(newRow, newCol) && isMine(newRow, newCol, realBoard)) {
                count++;
            }
        }

        return count;
    }

    static boolean placeMinesCount(char[][] playBoard, char[][] systemBoard, int[][] minePos,
                                       int row, int col, int[] movesLeft) {
        
        if(row==Integer.MAX_VALUE || col==Integer.MIN_VALUE){
            return false;
        }

        //if the player has already played for that position it will return false
         if (playBoard[row][col] != '-') {
            return false;
        }

        // if the player input position is found to be a position of mine then the player will lose the game and the gameOver will set to true

        if (systemBoard[row][col] == '*') {
            movesLeft[0]--;
            playBoard[row][col] = '*';

            for (int[] mine : minePos) {
                playBoard[mine[0]][mine[1]] = '*';
            }

            printBoard(playBoard);
            System.out.println("\nYOU LOST!");
            return true;

        } else {
            int count = countMines(row, col, minePos, systemBoard);
            movesLeft[0]--;

            playBoard[row][col] = (char) (count + '0');

            // if there is no surrounding mines around the user inputed position this block will again play for the other 8 position and it will call the function itself
            if(count==0){
                int directions[][]={{0, 1},{1, 1},{ 1, 0},{-1, 1},{-1, 0},{-1, -1},{0, -1},{1, -1}};
                for (int[] dir : directions) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];

                    if (isValid(newRow, newCol) && !isMine(newRow, newCol, systemBoard)) {
                        placeMinesCount(playBoard, systemBoard, minePos, newRow, newCol, movesLeft);
                    }
                }
            }
            return false;
        }
    }

//checks if the corresponding row and column fall in the matrix
    static boolean isValid(int row, int col) {
            boolean isAvailable = (row >= 0) && (row < side) && (col >= 0) && (col < side);
            return isAvailable;    
    }

    // checks if there is a mine present or not in the given row and column
    static boolean isMine(int row, int col, char[][] board) {
        if(systemBoard[row][col]=='*'){
            return true;
        }
        else{
            return false;
        }
    }

    static void makeMove(int[] move) {
       
        Scanner sc = new Scanner(System.in);
        int tag=1;
        boolean isTrue = true;
        boolean validMove = false;
            while (isTrue) {
                System.out.println("1)Redo the move   2)Make Move    3)Hint?   4)Exit");
                int choice = sc.nextInt();
                
                switch (choice) {
                    
                    case 1: if(movesLeft[0]<side*side-mines){
                        if (redoChance > 0) {
                            System.out.println("Redoing move");
                            redoChance--;
                            move[0] = Integer.MAX_VALUE;
                            move[1] = Integer.MAX_VALUE;
                             //to check if chaneses are reducing
                            System.out.println("Redo chances left "+redoChance);
                            getMoveRedo(dummyBoard, playBoard);
                        }
                         else {
                            System.out.println("No more redo chances.");
                            continue;
                        }
                    }
                        else{
                            System.out.println("You have not made any move");
                            continue;
                        }
                        break;
        
                    case 2:
                        while (!validMove) {
                            System.out.print("Enter your move, (row, column) -> ");
                            move[0] = sc.nextInt();
                            move[1] = sc.nextInt();
        
                            validMove = isValid(move[0], move[1]);
        
                            if (!validMove) {
                                System.out.println("Invalid move. Please try again.");
                                continue;
                            }
                            else{
                                isTrue = false;
                            }
                        }
                        
                        break;

                    case 3: if(hintChance>0){
                                giveHint(systemBoard);
                                hintChance--;
                                System.out.println("left hints "+hintChance);
                            }else{
                                System.out.println("There is no more hint left");
                            }
                            isTrue = false;
                            break;
                    case 4: System.out.println("Ending game");
                            exitGame();
                            break;

                    default:
                        System.out.println("Invalid choice. Please choose 1,2,3 or 4");
                }
            }
    }

    static void exitGame(){
        System.exit(0);
    }

    static void  getMoveRedo(char dummy[][], char playBoard[][]){
            for(int i=0;i<side;i++){
                for(int j=0;j<side;j++){
                    playBoard[i][j] = dummyBoard[i][j];
                }
            }
        System.out.println("your move is redo");
        printBoard(playBoard);
    }

    // the user here is given a chance to check whether there is a mine or not at the given input coordinates and it warns the user if mine is found.

    static void giveHint(char[][] realBoard){
        boolean isMoveValid = false;
        int hintRow = 0;
        int hintCol= 0;
        while(!isMoveValid){
        System.out.println("Enter ur position to know if there is a mine or not");
        hintRow = sc.nextInt();
        hintCol = sc.nextInt();
        isMoveValid = isValid(hintRow, hintCol);
        }

        if(realBoard[hintRow][hintCol]=='*'){
            System.out.println("Be safe! There is mine");
        }else{
            System.out.println("There is no mine");
        }
    }

    // It prints the playing Board.

    static void printBoard(char[][] playBoard) {
        System.out.print("  ");
        for (int i = 0; i < side; i++) {
            if(i/10<1)
                System.out.print(i + "  ");
            else
                System.out.print(i + " ");
        }

        System.out.print("\n");

        for (int i = 0; i < side; i++) {
            System.out.print(i + " ");

            for (int j = 0; j < side; j++) {
                if(j/10>=1)
                    System.out.print(playBoard[i][j] + "  ");
                else
                    System.out.print(playBoard[i][j] + "  ");
            }

            System.out.println();
        }
    }


public static void main(String[] args) {
        chooseDifficultyLevel(); // Change difficulty level here
        startGame();
    }
}
