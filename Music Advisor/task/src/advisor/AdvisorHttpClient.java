package advisor;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static advisor.EndPoints.ACCESS_TOKEN_REQUEST_PATH;
import static advisor.EndPoints.CATEGORIES_REQUEST_PATH;
import static advisor.EndPoints.DEFAULT_RESOURCE_PATH;
import static advisor.EndPoints.DEFAULT_SERVER_PATH;
import static advisor.EndPoints.FEATURED_REQUEST_PATH;
import static advisor.EndPoints.NEW_RELEASES_REQUEST_PATH;
import static advisor.EndPoints.PLAYLIST_REQUEST_PATH;
import static advisor.EndPoints.REDIRECT_URI;
import static advisor.SecureData.CLIENT_ID;
import static advisor.SecureData.CLIENT_SECRET;

public class AdvisorHttpClient {
    private String serverPath;
    private String resourcePath;
    private String accessToken;

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

    public boolean getAccessToken(String code) {
        System.out.println("Making http request for access_token...");

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(getServerPath() + ACCESS_TOKEN_REQUEST_PATH))
                .POST(HttpRequest.BodyPublishers.ofString(getAccessTokenRequest(code)))
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonElement accessTokenElement = jo.get("access_token");

            if (accessTokenElement == null) {
                JsonElement errorDescriptionElement = jo.get("error_description");
                if (errorDescriptionElement != null) {
                    System.out.println(errorDescriptionElement.getAsString());
                }
                return false;
            }

            accessToken = jo.get("access_token").getAsString();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error response");
            return false;
        }

        System.out.println("Success!");
        return true;
    }

    private String getAccessTokenRequest(String code) {
        return "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=authorization_code" +
                "&code=" + code +
                "&redirect_uri=" + REDIRECT_URI;
    }

    public List<String> getNew() {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(getResourcePath() + NEW_RELEASES_REQUEST_PATH))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            if (checkError(jo)) {
                return null;
            }

            List<String> list = new ArrayList<>();

            JsonArray albumElements = jo.get("albums").getAsJsonObject().get("items").getAsJsonArray();
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

                list.add(String.format("%s\n%s\n%s\n", name, artistList, href));
            }

            return list;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error response");
            return null;
        }
    }

    private boolean checkError(JsonObject jo) {
        JsonElement errorElement = jo.get("error");
        if (errorElement == null) {
            return false;
        }

        JsonObject error = errorElement.getAsJsonObject();
        JsonElement messageElement = error.get("message");
        if (messageElement != null) {
            System.out.println(messageElement.getAsString());
        } else {
            System.out.println("Error response");
        }

        return true;
    }

    public List<String> getCategories() {
        try {
            HttpResponse<String> response = getCategoriesResponse();

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            if (checkError(jo)) {
                return null;
            }

            List<String> list = new ArrayList<>();

            JsonArray elements = jo.get("categories").getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement element : elements) {
                list.add(element.getAsJsonObject().get("name").getAsString());
            }

            return list;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error response");
            return null;
        }
    }

    private HttpResponse<String> getCategoriesResponse() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(getResourcePath() + CATEGORIES_REQUEST_PATH))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Map<String, String> getCategoriesMap() {
        try {
            HttpResponse<String> response = getCategoriesResponse();

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            if (checkError(jo)) {
                return null;
            }

            Map<String, String> map = new HashMap<>();

            JsonArray elements = jo.get("categories").getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement element : elements) {
                JsonObject category = element.getAsJsonObject();
                map.put(category.get("name").getAsString(), category.get("id").getAsString());
            }

            return map;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error response");
            return null;
        }
    }

    public List<String> getFeatured() {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(getResourcePath() + FEATURED_REQUEST_PATH))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String>response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            if (checkError(jo)) {
                return null;
            }

            List<String> list = new ArrayList<>();

            JsonArray elements = jo.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement element : elements) {
                JsonObject playList = element.getAsJsonObject();

                String name = playList.get("name").getAsString();

                String href = playList.get("href").getAsString();
                if (!DEFAULT_RESOURCE_PATH.equals(getResourcePath())) {
                    href = href.replaceAll(DEFAULT_RESOURCE_PATH, getResourcePath());
                }

                list.add(String.format("%s\n%s\n", name, href));
            }

            return list;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error response");
            return null;
        }
    }

    public List<String> getPlaylist(String categoryName) {
        Map<String, String> categoriesMap = getCategoriesMap();
        if (categoriesMap == null) {
            return null;
        }
        String categoryId = categoriesMap.get(categoryName);
        if (categoryId == null) {
            System.out.println("Unknown category name.");
            return null;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(getResourcePath() + PLAYLIST_REQUEST_PATH.replace("{category_id}", categoryId)))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            if (checkError(jo)) {
                return null;
            }

            List<String> list = new ArrayList<>();

            JsonArray elements = jo.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
            for (JsonElement element : elements) {
                JsonObject playList = element.getAsJsonObject();

                String name = playList.get("name").getAsString();

                String href = playList.get("href").getAsString();
                if (!DEFAULT_RESOURCE_PATH.equals(getResourcePath())) {
                    href = href.replaceAll(DEFAULT_RESOURCE_PATH, getResourcePath());
                }

                list.add(String.format("%s\n%s\n", name, href));
            }

            return list;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error response");
            return null;
        }
    }
}
