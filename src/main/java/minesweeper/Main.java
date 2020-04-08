package minesweeper;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many mines do you want on the field? ");
        int mines = scanner.nextInt();
        GameEngine game = new GameEngine(mines);
        printBoard(game.getBoard());

        boolean finished = false;
        while (!finished) {
            int row;
            int col;
            boolean inputCorrect = true;
            do {
                if (!inputCorrect) {
                    System.out.println("There is a number here!");
                }

                System.out.print("Set/delete mines marks (x and y coordinates): ");
                col = scanner.nextInt();
                row = scanner.nextInt();
            } while (!(inputCorrect = game.markField(row - 1, col - 1)));

            System.out.println();
            printBoard(game.getBoard());

            if (game.checkVictory()) {
                System.out.println("Congratulations! You found all mines!");
                finished = true;
            }
        }
    }

    private static void printBoard(int[][] board) {
        System.out.print("  |");
        for (int i = 0; i < board.length; i++) {
            System.out.printf("%3d", i + 1);
        }
        System.out.println("|");

        System.out.print("--|");
        for (int i = 0; i < board.length; i++) {
            System.out.printf("---");
        }
        System.out.println("|");

        for (int i = 0; i < board.length; i++) {
            System.out.printf("%2d|", i + 1);
            for (int cell : board[i]) {
                if (cell == -2) {
                    System.out.print("  *");
                } else if  (cell <= 0) {
                    System.out.print("  .");
                } else {
                    System.out.printf("%3d", cell);
                }

            }
            System.out.println("|");
        }

        System.out.print("--|");
        for (int i = 0; i < board.length; i++) {
            System.out.print("---");
        }
        System.out.println("|");
    }
}
