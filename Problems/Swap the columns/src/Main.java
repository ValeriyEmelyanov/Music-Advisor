import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt();
        final int width = scanner.nextInt();
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
        final int column1 = scanner.nextInt();
        final int column2 = scanner.nextInt();
        scanner.close();

        if (column1 < 0 || column1 >= width
                || column2 < 0 || column2 >= width) {
            throw new IllegalArgumentException("Invalid column index");
        }

        swapColumns(matrix, column1, column2);

        printMatrix(matrix);
    }

    private static void swapColumns(int[][] matrix, int column1, int column2) {
        if (column1 != column2) {
            for (int[] ints : matrix) {
                ints[column1] = ints[column1] ^ ints[column2];
                ints[column2] = ints[column1] ^ ints[column2];
                ints[column1] = ints[column1] ^ ints[column2];
            }
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int i : ints) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}