package advisor;

import java.util.Scanner;

public class Main {
    private static final String CLIENT_ID = "0dc73b190af34720b5ef1541c17df349";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize"
        + "?client_id=%s&redirect_uri=http://localhost:8080&response_type=code";

    private boolean isAuth = false;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        final Scanner scanner = new Scanner(System.in);

        String request;
        while (scanner.hasNext()) {
            request = scanner.nextLine().trim();

            if (!isAuth && !request.equals("auth")) {
                System.out.println("Please, provide access for application.");
                continue;
            }

            String[] requestWords = request.split("\\s+");
            switch (requestWords[0]) {
                case "auth":
                    auth();
                    break;
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
                    scanner.close();
                    System.out.println("---GOODBYE!---");
                    return;
                default:
                    System.out.println("Invalid request! Try again");
            }
        }
    }

    private void auth() {
        isAuth = true;
        System.out.println(String.format(AUTH_URL, CLIENT_ID));
        System.out.println("---SUCCESS---");
    }

    private void printPlaylists(String requestWord) {
        System.out.println("---MOOD PLAYLISTS---");
        System.out.println("Walk Like A Badass");
        System.out.println("Rage Beats");
        System.out.println("Arab Mood Booster");
        System.out.println("Sunday Stroll");
    }

    private void printCategories() {
        System.out.println("---CATEGORIES---");
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("Latin");
    }

    private void printFeatured() {
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }

    private void printNew() {
        System.out.println("---NEW RELEASES---");
        System.out.println("Mountains [Sia, Diplo, Labrinth]");
        System.out.println("Runaway [Lil Peep]");
        System.out.println("The Greatest Show [Panic! At The Disco]");
        System.out.println("All Out Life [Slipknot]");
    }
}
