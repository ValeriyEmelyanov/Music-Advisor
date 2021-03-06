package advisor.config;

public class EndPoints {

    public static final String REDIRECT_URI = "http://localhost:8080";
    public static final String AUTH_URL = "https://accounts.spotify.com/authorize"
            + "?client_id=%s&redirect_uri=http://localhost:8080&response_type=code";

    public static final String DEFAULT_SERVER_PATH = "https://accounts.spotify.com";
    public static final String DEFAULT_RESOURCE_PATH = "https://api.spotify.com";

    public static final String ACCESS_TOKEN_REQUEST_PATH = "/api/token";

    public static final String CATEGORIES_REQUEST_PATH = "/v1/browse/categories?limit={limit}";
    public static final String PLAYLIST_REQUEST_PATH = "/v1/browse/categories/{category_id}/playlists?limit={limit}";
    public static final String NEW_RELEASES_REQUEST_PATH = "/v1/browse/new-releases?offset=0&limit={limit}";
    public static final String FEATURED_REQUEST_PATH = "/v1/browse/featured-playlists?limit={limit}";
}
