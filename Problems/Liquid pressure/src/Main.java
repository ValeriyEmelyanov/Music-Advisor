import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double density = scanner.nextDouble();
        double height = scanner.nextDouble();

        final double gravity = 9.8;
        double pressure = density * gravity * height;

        System.out.println(pressure);
    }
}