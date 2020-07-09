package advisor.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String name = "";
    private List<String> artists = new ArrayList<>();
    private String href = "";

    public Album(String name, List<String> artists, String href) {
        this.name = name;
        this.artists = artists;
        this.href = href;
    }

    @Override
    public String toString() {
        return name + "\n" +
                artists + "\n" +
                href + "\n";
    }
}
