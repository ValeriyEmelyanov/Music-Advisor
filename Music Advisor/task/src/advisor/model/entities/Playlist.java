package advisor.model.entities;

public class Playlist {
    private String name = "";
    private String href = "";

    public Playlist(String name, String href) {
        this.name = name;
        this.href = href;
    }

    @Override
    public String toString() {
        return name + "\n" +
                href + "\n";
    }
}
