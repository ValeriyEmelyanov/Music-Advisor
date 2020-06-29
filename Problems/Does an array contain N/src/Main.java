import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int len = scanner.nextInt();
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = scanner.nextInt();
        }
        final int num = scanner.nextInt();
        scanner.close();

        System.out.println(contains(arr, num));
    }

    private static boolean contains(int[] arr, int num) {
        for (int i : arr) {
            if (i == num) {
                return true;
            }
        }
        return false;
    }
}