package advisor;

public class EndPoints {

    public static final String REDIRECT_URI = "http://localhost:8080";
    public static final String AUTH_URL = "https://accounts.spotify.com/authorize"
            + "?client_id=%s&redirect_uri=http://localhost:8080&response_type=code";

    public static final String DEFAULT_SERVER_PATH = "https://accounts.spotify.com";
    public static final String ACCESS_TOKEN_REQUEST_PATH = "/api/token";
}
