import java.util.*;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        int[] box1 = getBoxDimensions(scanner);
        int[] box2 = getBoxDimensions(scanner);
        scanner.close();

        Arrays.sort(box1);
        Arrays.sort(box2);

        if (isEqual(box1, box2)) {
            System.out.println("Box 1 = Box 2");
        } else if (isGreater(box1, box2)) {
            System.out.println("Box 1 > Box 2");
        } else if (isGreater(box2, box1)) {
            System.out.println("Box 1 < Box 2");
        } else {
            System.out.println("Incomparable");
        }

    }

    private static int[] getBoxDimensions(Scanner scanner) {
        final int numberOfDimensions = 3;
        int[] box = new int[numberOfDimensions];
        for (int i = 0; i < numberOfDimensions; i++) {
            box[i] = scanner.nextInt();
        }
        return box;
    }

    private static boolean isEqual(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGreater(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] < arr2[i]) {
                return false;
            }
        }
        return true;
    }
}