package advisor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static advisor.ConstData.*;

public class AdvisorHttpClient {
    private final HttpClient client;

    public AdvisorHttpClient() {
        client = HttpClient.newBuilder().build();
    }

    public void getAccessToken(String serverPath, String code) throws IOException, InterruptedException {
        System.out.println("making http request for access_token...");

        URI uri = URI.create(
                (serverPath == null || serverPath.isEmpty() ? SERVER_PATH : serverPath)
                        + ACCESS_TOKEN_REQUEST_PATH);
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(getAccessTokenRequest(code)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("response:");
        System.out.println(response.body());
    }

    private String getAccessTokenRequest(String code) {
        return new StringBuilder()
                .append("client_id=").append(CLIENT_ID)
                .append("&client_secret=").append(CLIENT_SECRET)
                .append("&grant_type=authorization_code")
                .append("&code=").append(code)
                .append("&redirect_uri=").append(REDIRECT_URI)
                .toString();
    }
}
