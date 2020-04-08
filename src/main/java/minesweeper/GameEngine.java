package minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameEngine {

    private static final int DEFAULT_SIZE = 9;

    private static final int DEFAULT_MINES_NUMBER = 10;

    private int[][] board;

    private int[][] userBoard;

    private int size;

    private int minesNumber;

    private int minesMarked = 0;

    private int fieldsMarked = 0;

    private int fieldsRevealed = 0;

    private boolean gameLost = false;

    private Set<Integer> mineIndexes = new HashSet<>();

    private Random random = new Random();

    public GameEngine(int size, int minesNumber) {
        this.board = new int[size][size];
        this.userBoard = new int[size][size];
        this.size = size;
        this.minesNumber = minesNumber;
        fillBoard(minesNumber);
        fillUserBoard();
    }

    public GameEngine(int minesNumber) {
        this(DEFAULT_SIZE, minesNumber);
    }

    public GameEngine() {
        this(DEFAULT_SIZE, DEFAULT_MINES_NUMBER);
    }

    public int[][] getUserBoard() {
        return userBoard;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    private void fillBoard(int minesNumber) {
        int cellsNumber = size * size;
        if (minesNumber < 0 || minesNumber > cellsNumber) {
            throw new IllegalArgumentException("Number of mines must be non-negative and cannot exceed number of board cells");
        }

        int count = 0;
        while (count < minesNumber) {
            int cell = random.nextInt(cellsNumber);
            int row = cell / size;
            int col = cell % size;
            if (board[row][col] == 0) {
                board[row][col] = -1;
                mineIndexes.add(cell);
                count++;
            }
        }

        countMines();
    }

    private void fillUserBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                userBoard[i][j] = -1;
            }
        }
    }

    private void countMines() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = (board[i][j] == 0) ? countAdjacentMines(i, j) : -1;
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int rowStart = Math.max(0, row - 1);
        int rowEnd = Math.min(size - 1, row + 1);
        int colStart = Math.max(0, col - 1);
        int colEnd = Math.min(size - 1, col + 1);

        int count = 0;
        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                if (board[i][j] == -1) {
                    count++;
                }
            }
        }

        if (board[row][col] == -1) {
            count--;
        }

        return count;
    }

    public boolean markField(int row, int col) {
        // if the field is already revealed do nothing
        if (userBoard[row][col] >= 0) {
            return false;
        }

        if (userBoard[row][col] == -1) { // set the field marked
            userBoard[row][col] = -2;
            fieldsMarked++;
            if (board[row][col] == -1) {
                minesMarked++;
            }
        } else { // set the field unmarked
            userBoard[row][col] = -1;
            fieldsMarked--;
            if (board[row][col] == -1) {
                minesMarked--;
            }
        }

        return true;
    }

    public boolean revealField(int row, int col) {
        // if the field is already revealed do nothing
        if (userBoard[row][col] >= 0) {
            return false;
        }

        if (board[row][col] > 0) {
            userBoard[row][col] = board[row][col];
            fieldsRevealed++;
        } else if (board[row][col] == 0) {
            userBoard[row][col] = 0;
            fieldsRevealed++;
            revealAdjacentFields(row, col);
        } else {
            gameLost = true;
            revealMines();
        }

        return true;
    }

    private void revealAdjacentFields(int row, int col) {
        for (int i = Math.max(0, row - 1); i <= Math.min(size - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(size - 1, col + 1); j++) {
                if (userBoard[i][j] >= 0) {
                    continue;
                }

                if (board[i][j] > 0) {
                    userBoard[i][j] = board[i][j];
                    fieldsRevealed++;
                } else if (board[i][j] == 0) {
                    userBoard[i][j] = 0;
                    fieldsRevealed++;
                    revealAdjacentFields(i, j);
                }
            }
        }
    }

    private void revealMines() {
        mineIndexes.forEach(index -> userBoard[index / size][index % size] = -3);
    }

    public boolean checkVictory() {
        return (minesMarked == minesNumber && fieldsMarked == minesNumber) || (fieldsRevealed + minesNumber == size * size);
    }
}
