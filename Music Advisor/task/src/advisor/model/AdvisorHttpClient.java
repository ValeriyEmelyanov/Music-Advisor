package advisor.model;

import advisor.model.entities.Album;
import advisor.model.entities.Category;
import advisor.model.entities.Playlist;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static advisor.config.EndPoints.ACCESS_TOKEN_REQUEST_PATH;
import static advisor.config.EndPoints.CATEGORIES_REQUEST_PATH;
import static advisor.config.EndPoints.DEFAULT_RESOURCE_PATH;
import static advisor.config.EndPoints.DEFAULT_SERVER_PATH;
import static advisor.config.EndPoints.FEATURED_REQUEST_PATH;
import static advisor.config.EndPoints.NEW_RELEASES_REQUEST_PATH;
import static advisor.config.EndPoints.PLAYLIST_REQUEST_PATH;
import static advisor.config.EndPoints.REDIRECT_URI;
import static advisor.config.SecureData.CLIENT_ID;
import static advisor.config.SecureData.CLIENT_SECRET;

public class AdvisorHttpClient {
    private static final AdvisorHttpClient INSTANCE = new AdvisorHttpClient();

    private String serverPath;
    private String resourcePath;
    private String accessToken;
    private String limit = "5";

    private AdvisorHttpClient() {
    }

    public static AdvisorHttpClient getInstance() {
        return INSTANCE;
    }

    public String getServerPath() {
        return serverPath == null || serverPath.isEmpty() ? DEFAULT_SERVER_PATH : serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getResourcePath() {
        return resourcePath == null || resourcePath.isEmpty() ? DEFAULT_RESOURCE_PATH : resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public boolean getAccessToken(String code) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(getServerPath() + ACCESS_TOKEN_REQUEST_PATH))
                .POST(HttpRequest.BodyPublishers.ofString(getAccessTokenRequest(code)))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonElement accessTokenElement = jo.get("access_token");

        if (accessTokenElement == null) {
            JsonElement errorDescriptionElement = jo.get("error_description");
            if (errorDescriptionElement != null) {
                throw new IllegalArgumentException(errorDescriptionElement.getAsString());
            } else {
                throw new IllegalArgumentException("Error response");
            }
        }

        accessToken = jo.get("access_token").getAsString();

        return true;
    }

    private String getAccessTokenRequest(String code) {
        return "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=authorization_code" +
                "&code=" + code +
                "&redirect_uri=" + REDIRECT_URI;
    }

    private void checkError(JsonObject jo) throws IOException {
        JsonElement errorElement = jo.get("error");
        if (errorElement == null) {
            return;
        }

        JsonObject error = errorElement.getAsJsonObject();
        JsonElement messageElement = error.get("message");
        String message;
        if (messageElement != null) {
            message = messageElement.getAsString();
        } else {
            message = "Error response";
        }

        throw new IOException(message);
    }

    public Page<Album> getNewAlbums() throws IOException, InterruptedException {
        return getNewAlbums(getResourcePath() + NEW_RELEASES_REQUEST_PATH.replaceAll("\\{limit}", limit));
    }

    public Page<Album> getNewAlbums(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        checkError(jo);

        List<Album> list = new ArrayList<>();

        JsonObject albums = jo.get("albums").getAsJsonObject();

        JsonArray albumElements = albums.get("items").getAsJsonArray();
        for (JsonElement albumElement : albumElements) {
            JsonObject album = albumElement.getAsJsonObject();

            String name = album.get("name").getAsString();

            String href = album.get("href").getAsString();
            if (!DEFAULT_RESOURCE_PATH.equals(getResourcePath())) {
                href = href.replaceAll(DEFAULT_RESOURCE_PATH, getResourcePath());
            }

            List<String> artistList = new ArrayList<>();
            JsonArray artistElements = album.get("artists").getAsJsonArray();
            for (JsonElement artistElement : artistElements) {
                JsonObject artist = artistElement.getAsJsonObject();
                artistList.add(artist.get("name").getAsString());
            }

            list.add(new Album(name, artistList, href));
        }

        JsonElement next = albums.get("next");
        String nextRef = next.isJsonNull() ? null : next.getAsString();
        JsonElement previous = albums.get("previous");
        String previousRef = previous.isJsonNull() ? null : previous.getAsString();

        return new Page<>(
                list,
                albums.get("limit").getAsInt(),
                albums.get("offset").getAsInt(),
                albums.get("total").getAsInt(),
                nextRef,
                previousRef);
    }

    public Page<Category> getCategories() throws IOException, InterruptedException {
        return getCategories(getResourcePath() + CATEGORIES_REQUEST_PATH.replaceAll("\\{limit}", limit));
    }

    public Page<Category> getCategories(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        checkError(jo);

        List<Category> list = new ArrayList<>();

        JsonObject categories = jo.get("categories").getAsJsonObject();
        JsonArray elements = categories.get("items").getAsJsonArray();
        for (JsonElement element : elements) {
            JsonObject category = element.getAsJsonObject();
            list.add(new Category(
                    category.get("id").getAsString(),
                    category.get("name").getAsString()));
        }

        JsonElement next = categories.get("next");
        String nextRef = next.isJsonNull() ? null : next.getAsString();
        JsonElement previous = categories.get("previous");
        String previousRef = previous.isJsonNull() ? null : previous.getAsString();

        return new Page<>(
                list,
                categories.get("limit").getAsInt(),
                categories.get("offset").getAsInt(),
                categories.get("total").getAsInt(),
                nextRef,
                previousRef);
    }

    public Page<Playlist> getFeatured() throws IOException, InterruptedException {
        return getFeatured(getResourcePath() + FEATURED_REQUEST_PATH.replaceAll("\\{limit}", limit));
    }

    public Page<Playlist> getFeatured(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        checkError(jo);

        JsonObject playlists = jo.get("playlists").getAsJsonObject();

        List<Playlist> list = new ArrayList<>();
        JsonArray elements = playlists.get("items").getAsJsonArray();
        for (JsonElement element : elements) {
            JsonObject playList = element.getAsJsonObject();

            String name = playList.get("name").getAsString();

            String href = playList.get("href").getAsString();
            if (!DEFAULT_RESOURCE_PATH.equals(getResourcePath())) {
                href = href.replaceAll(DEFAULT_RESOURCE_PATH, getResourcePath());
            }

            list.add(new Playlist(name, href));
        }

        JsonElement next = playlists.get("next");
        String nextRef = next.isJsonNull() ? null : next.getAsString();
        JsonElement previous = playlists.get("previous");
        String previousRef = previous.isJsonNull() ? null : previous.getAsString();

        return new Page<>(
                list,
                playlists.get("limit").getAsInt(),
                playlists.get("offset").getAsInt(),
                playlists.get("total").getAsInt(),
                nextRef,
                previousRef);
    }

    public Page<Playlist> getPlaylistByCategoryName(String categoryName) throws IOException, InterruptedException {

        String categoryId = getCategoryId(categoryName);
        return getPlaylist(getResourcePath() +
                        PLAYLIST_REQUEST_PATH
                                .replace("{category_id}", categoryId)
                                .replaceAll("\\{limit}", limit));

    }

    public Page<Playlist> getPlaylist(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        checkError(jo);

        List<Playlist> list = new ArrayList<>();

        JsonObject playlists = jo.get("playlists").getAsJsonObject();
        JsonArray elements = playlists.get("items").getAsJsonArray();
        for (JsonElement element : elements) {
            JsonObject playList = element.getAsJsonObject();

            String name = playList.get("name").getAsString();

            String href = playList.get("href").getAsString();
            if (!DEFAULT_RESOURCE_PATH.equals(getResourcePath())) {
                href = href.replaceAll(DEFAULT_RESOURCE_PATH, getResourcePath());
            }

            list.add(new Playlist(name, href));
        }

        JsonElement next = playlists.get("next");
        String nextRef = next.isJsonNull() ? null : next.getAsString();
        JsonElement previous = playlists.get("previous");
        String previousRef = previous.isJsonNull() ? null : previous.getAsString();

        return new Page<>(
                list,
                playlists.get("limit").getAsInt(),
                playlists.get("offset").getAsInt(),
                playlists.get("total").getAsInt(),
                nextRef,
                previousRef);
    }

    private String getCategoryId(String categoryName) throws IOException, InterruptedException {
        if (categoryName == null) {
            throw new IllegalArgumentException("Unknown category name.");
        }

        Page<Category> page = null;
        while (true) {
            if (page != null && page.getNext() == null) {
                throw new IOException("Unknown category name.");
            }

            if (page == null) {
                page = getCategories();
            } else {
                page = getCategories(page.getNext());
            }

            List<Category> categories = page.getList();
            Category category = categories.stream()
                    .filter(c -> categoryName.equalsIgnoreCase(c.getName()))
                    .findFirst()
                    .orElse(null);

            if (category != null) {
                return category.getId();
            }
        }

    }
}
