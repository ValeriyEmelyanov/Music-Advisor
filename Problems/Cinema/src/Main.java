import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt();
        final int width = scanner.nextInt();
        final int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
        final int seqLen = scanner.nextInt();
        scanner.close();

        System.out.println(getRowNumber(matrix, seqLen));
    }

    private static int getRowNumber(int[][] matrix, int seqLen) {
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    count = 0;
                    continue;
                }
                count++;
                if (count == seqLen) {
                    return i + 1;
                }
            }
            count = 0;
        }
        return 0;
    }
}