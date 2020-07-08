import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String input = scanner.nextLine();
        scanner.close();

        System.out.println(encode(input));
    }

    private static String encode(String input) {
        if (input == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        char[] chars = input.toCharArray();
        char current = chars[0];
        int count  = 0;
        for (char c : input.toCharArray()) {
            if (current == c) {
                count++;
            } else {
                builder.append(current).append(count);
                current = c;
                count = 1;
            }
        }
        builder.append(current).append(count);
        return builder.toString();
    }
}