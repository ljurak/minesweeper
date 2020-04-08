package minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameEngine {

    private static final int DEFAULT_SIZE = 9;

    private static final int DEFAULT_MINES_NUMBER = 10;

    private int[][] board;

    private int size;

    private int minesNumber;

    private int minesMarked = 0;

    private int fieldsMarked = 0;

    private Set<Integer> mineIndexes = new HashSet<>();

    private Random random = new Random();

    public GameEngine(int size, int minesNumber) {
        this.board = new int[size][size];
        this.size = size;
        this.minesNumber = minesNumber;
        fillBoard(minesNumber);
    }

    public GameEngine(int minesNumber) {
        this(DEFAULT_SIZE, minesNumber);
    }

    public GameEngine() {
        this(DEFAULT_SIZE, DEFAULT_MINES_NUMBER);
    }

    public int[][] getBoard() {
        return board;
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
        // cannot mark field with positive number, they are guaranteed to be free of mines
        if (board[row][col] > 0) {
            return false;
        } else if (board[row][col] == -2) { // unmark field
            if (mineIndexes.contains(row * size + col)) {
                board[row][col] = -1;
                minesMarked--;
            } else {
                board[row][col] = 0;
            }
            fieldsMarked--;
        } else { // mark field
            board[row][col] = -2;
            fieldsMarked++;
            if (mineIndexes.contains(row * size + col)) {
                minesMarked++;
            }
        }

        return true;
    }

    public boolean checkVictory() {
        return minesMarked == minesNumber && fieldsMarked == minesNumber;
    }
}
