package minesweeper;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern pattern = Pattern.compile("\\d+ \\d+ (mine|free)");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        int mines = Integer.parseInt(scanner.nextLine());
        GameEngine game = new GameEngine(mines);

        printBoard(game.getUserBoard());

        boolean finished = false;
        while (!finished) {
            String input;
            do {
                System.out.print("Set/unset mines marks or claim a cell as free: ");
                input = scanner.nextLine();
            } while (!pattern.matcher(input).matches());

            String[] inputParts = input.split(" ");
            int col = Integer.parseInt(inputParts[0]);
            int row = Integer.parseInt(inputParts[1]);
            String mode = inputParts[2];

            switch (mode) {
                case "mine":
                    game.markField(row - 1, col - 1);
                    break;
                case "free":
                    game.revealField(row - 1, col - 1);
                    break;
                default:
                    throw new IllegalStateException("Unsupported game mode");
            }

            System.out.println();
            printBoard(game.getUserBoard());

            if (game.isGameLost()) {
                System.out.println("You stepped on a mine and failed!");
                finished = true;
            }

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
            System.out.print("---");
        }
        System.out.println("|");

        for (int i = 0; i < board.length; i++) {
            System.out.printf("%2d|", i + 1);
            for (int cell : board[i]) {
                if (cell == -3) {
                    System.out.print("  X");
                } else if (cell == -2) {
                    System.out.print("  *");
                } else if  (cell == -1) {
                    System.out.print("  .");
                } else if (cell == 0) {
                    System.out.print("  /");
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
