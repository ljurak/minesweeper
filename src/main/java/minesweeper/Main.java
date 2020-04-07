package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        int mines = scanner.nextInt();
        byte[][] board = new byte[9][9];
        initBoard(board, mines);
        printBoard(board);
    }

    private static void initBoard(byte[][] board, int mines) {
        Random random = new Random();

        int count = 0;
        while (count < mines) {
            int cell = random.nextInt(81);
            if (board[cell / 9][cell % 9] == 0) {
                board[cell / 9][cell % 9] = 1;
                count++;
            }
        }
    }

    private static void printBoard(byte[][] board) {
        for (byte[] row : board) {
            for (byte cell : row) {
                System.out.print(cell == 0 ? "." : "X");
            }
            System.out.println();
        }
    }
}
