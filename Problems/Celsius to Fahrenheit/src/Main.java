import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Double celsius = scanner.nextDouble();
        scanner.close();

        Double fahrenheit = celsius * 1.8 + 32;

        System.out.println(fahrenheit);
    }
}