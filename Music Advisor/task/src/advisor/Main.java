package advisor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private boolean isAuth = false;
    private final AdvisorHttpClient httpClient = new AdvisorHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> argsList = new ArrayList<>();
        for (String arg : args) {
            argsList.addAll(Arrays.asList(arg.split("[\\u00A0\\s]+")));
        }
        if (argsList.size() % 2 != 0) {
            throw new IllegalArgumentException("Wrong argument number!");
        }

        Main main = new Main();
        for (int i = 0; i < argsList.size(); i += 2) {
            if ("-access".equals(argsList.get(i))) {
                main.httpClient.setServerPath(argsList.get(i + 1));
            }
            if ("-resource".equals(argsList.get(i))) {
                main.httpClient.setResourcePath(argsList.get(i + 1));
            }
        }
        main.run();
    }

    public void run() throws IOException, InterruptedException {
        final Scanner scanner = new Scanner(System.in);

        String request;
        while (scanner.hasNext()) {
            request = scanner.nextLine().trim();

            if ("exit".equals(request)) {
                scanner.close();
                System.out.println("Goodbye!");
                return;
            }

            if (!isAuth && !"auth".equals(request)) {
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
                    String name = request.substring(requestWords[0].length()).trim();
                    printPlaylists(name);
                    break;
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

        isAuth = httpClient.getAccessToken(code);
    }

    private void printPlaylists(String name) {
        List<String> list = httpClient.getPlaylist(name);
        if (list == null) {
            return;
        }

        list.forEach(System.out::println);
    }

    private void printCategories() {
        List<String> list = httpClient.getCategories();
        if (list == null) {
            return;
        }

        list.forEach(System.out::println);
    }

    private void printFeatured() {
        List<String> list = httpClient.getFeatured();
        if (list == null) {
            return;
        }

        list.forEach(System.out::println);
    }

    private void printNew() {
        List<String> list = httpClient.getNew();
        if (list == null) {
            return;
        }

        list.forEach(System.out::println);
    }
}
