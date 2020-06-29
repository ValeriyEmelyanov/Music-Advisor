import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        String seq = scanner.nextLine();

        char[] chars = seq.toLowerCase().toCharArray();
        int count = 0;
        for (char c : chars) {
            if (c == 'g' || c == 'c') {
                count++;
            }
        }
        double result = count * 100.0 / chars.length;

        System.out.println(result);
    }
}