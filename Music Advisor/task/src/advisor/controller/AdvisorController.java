package advisor.controller;

import advisor.model.AdvisorHttpClient;
import advisor.model.CodeReceiver;
import advisor.model.Page;
import advisor.model.entities.Album;
import advisor.model.entities.Category;
import advisor.model.entities.Playlist;
import advisor.model.navigatestrategy.CategoriesNavigator;
import advisor.model.navigatestrategy.FeaturedNavigator;
import advisor.model.navigatestrategy.Navigator;
import advisor.model.navigatestrategy.NewNavigator;
import advisor.model.navigatestrategy.PlaylistsNavigator;
import advisor.veiw.ConsoleView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static advisor.config.EndPoints.AUTH_URL;
import static advisor.config.SecureData.CLIENT_ID;

public class AdvisorController {
    private boolean isAuth = false;
    private final AdvisorHttpClient httpClient = AdvisorHttpClient.getInstance();
    private final ConsoleView view = ConsoleView.getInstance();
    private Navigator navigator = null;

    public void setServerPath(String serverPath) {
        httpClient.setServerPath(serverPath);
    }

    public void setResourcePath(String resourcePath) {
        httpClient.setResourcePath(resourcePath);
    }

    public void setLimit(String limit) {
        httpClient.setLimit(limit);
    }

    public void run() throws IOException, InterruptedException {
        final Scanner scanner = new Scanner(System.in);

        String request;
        while (scanner.hasNext()) {
            request = scanner.nextLine().trim();

            if ("exit".equals(request)) {
                scanner.close();
                view.showMessage("Goodbye!");
                return;
            }

            if (!isAuth && !"auth".equals(request)) {
                view.showMessage("Please, provide access for application.");
                continue;
            }

            String[] requestWords = request.split("\\s+");
            switch (requestWords[0]) {
                case "auth":
                    auth();
                    break;
                case "new":
                    newAlbums();
                    break;
                case "featured":
                    featured();
                    break;
                case "categories":
                    categories();
                    break;
                case "playlists":
                    String name = request.substring(requestWords[0].length()).trim();
                    playlists(name);
                    break;
                case "next":
                    next();
                    break;
                case "prev":
                    prev();
                    break;
                default:
                    view.showMessage("Invalid request! Try again");
            }
        }
    }

    private void auth() throws IOException, InterruptedException {
        CodeReceiver codeReceiver = new CodeReceiver();

        view.showMessage("use this link to request the access code:");
        view.showMessage(String.format(AUTH_URL, CLIENT_ID));
        view.showMessage("waiting for code...");

        codeReceiver.run();
        if (codeReceiver.isSuccess()) {
            view.showMessage("code received");
        } else {
            view.showMessage("Auth is failed. Try again.");
            return;
        }
        String code = codeReceiver.getCode();

        view.showMessage("Making http request for access_token...");
        try {
            isAuth = httpClient.getAccessToken(code);
            view.showMessage("Success!");
        } catch (IOException | InterruptedException e) {
            view.showMessage("Error response");
        } catch (IllegalArgumentException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void newAlbums() {
        try {
            Page<Album> page = httpClient.getNewAlbums();
            navigator = new NewNavigator(page.getNext(), page.getPrevious());
            view.showPage(page);
        } catch (IOException | InterruptedException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void categories() {
        try {
            Page<Category> page = httpClient.getCategories();
            navigator = new CategoriesNavigator(page.getNext(), page.getPrevious());
            view.showPage(page);
        } catch (IOException | InterruptedException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void featured() {
        try {
            Page<Playlist> page = httpClient.getFeatured();
            navigator = new FeaturedNavigator(page.getNext(), page.getPrevious());
            view.showPage(page);
        } catch (IOException | InterruptedException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void playlists(String name) {
        try {
            Page<Playlist> page = httpClient.getPlaylistByCategoryName(name);
            navigator = new PlaylistsNavigator(page.getNext(), page.getPrevious());
            view.showPage(page);
        } catch (IOException | InterruptedException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void next() {
        if (navigator == null || !navigator.hasNext()) {
            view.showMessage("No more pages.");
            return;
        }

        try {
            Page<?> page = navigator.next();
            navigator.setNext(page.getNext());
            navigator.setPrevious(page.getPrevious());
            view.showPage(page);
        } catch (IOException | InterruptedException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void prev() {
        if (navigator == null || !navigator.hasPrev()) {
            view.showMessage("No more pages.");
            return;
        }

        try {
            Page<?> page = navigator.prev();
            navigator.setNext(page.getNext());
            navigator.setPrevious(page.getPrevious());
            view.showPage(page);
        } catch (IOException | InterruptedException e) {
            view.showMessage(e.getMessage());
        }
    }

}
