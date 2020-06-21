package advisor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        String request;
        while (!exit) {
            request = scanner.nextLine();
            String[] requestWords = request.split("\\s+");
            switch (requestWords[0]) {
                case "new":
                    printNew();
                    break;
                case "featured":
                    printFeatured();
                    break;
                case "categories":
                    printCategories();
                    break;
                case "playlists":
                    printPlaylists(requestWords[1]);
                    break;
                case "exit":
                    exit = true;
                    System.out.println("---GOODBYE!---");
                    break;
                default:
                    System.out.println("Invalid request! Try again");
            }
        }
    }

    private static void printPlaylists(String requestWord) {
        System.out.println("---MOOD PLAYLISTS---");
        System.out.println("Walk Like A Badass");
        System.out.println("Rage Beats");
        System.out.println("Arab Mood Booster");
        System.out.println("Sunday Stroll");
    }

    private static void printCategories() {
        System.out.println("---CATEGORIES---");
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("Latin");
    }

    private static void printFeatured() {
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }

    private static void printNew() {
        System.out.println("---NEW RELEASES---");
        System.out.println("Mountains [Sia, Diplo, Labrinth]");
        System.out.println("Runaway [Lil Peep]");
        System.out.println("The Greatest Show [Panic! At The Disco]");
        System.out.println("All Out Life [Slipknot]");
    }
}
