package advisor;

public class ConstData {
    public static final String CLIENT_ID = "0dc73b190af34720b5ef1541c17df349";
    public static final String CLIENT_SECRET = "844fd7b00d9549a29e716bf89254993e";

    public static final String REDIRECT_URI = "http://localhost:8080";
    public static final String AUTH_URL = "https://accounts.spotify.com/authorize"
            + "?client_id=%s&redirect_uri=http://localhost:8080&response_type=code";

    public static final String SERVER_PATH = "https://accounts.spotify.com";
    public static final String ACCESS_TOKEN_REQUEST_PATH = "/api/token";
}
