import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        String[] strings = scanner.nextLine().split("\\s+");
        scanner.close();

        System.out.println(checkOrder(strings));
    }

    private static boolean checkOrder(String[] strings) {
        for (int i = 0; i < strings.length - 1; i++) {
            if (strings[i].compareTo(strings[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }
}