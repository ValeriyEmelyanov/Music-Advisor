package advisor;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private String serverPath;
    private boolean isAuth = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        if (args.length == 2 && "-access".equals(args[0])) {
            main.setServerPath(args[1]);
        }
        main.run();
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public void run() throws IOException, InterruptedException {
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

    private void auth() throws IOException, InterruptedException {
        CodeReceiver codeReceiver = new CodeReceiver();
        codeReceiver.run();
        if (!codeReceiver.isSuccess()) {
            System.out.println("Auth is failed. Try again.");
            return;
        }
        String code = codeReceiver.getCode();

        AdvisorHttpClient client = new AdvisorHttpClient();
        client.getAccessToken(serverPath, code);

        isAuth = true;
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
