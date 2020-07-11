package advisor.model.navigatestrategy;

import advisor.model.AdvisorHttpClient;
import advisor.model.Page;

import java.io.IOException;

public class NewNavigator extends AbstractNavigator {

    public NewNavigator(String next, String previous) {
        super(next, previous);
    }

    @Override
    public Page<?> next() throws IOException, InterruptedException {
        return ADVISOR_HTTP_CLIENT.getNewAlbums(next);
    }

    @Override
    public Page<?> prev() throws IOException, InterruptedException {
        return ADVISOR_HTTP_CLIENT.getNewAlbums(previous);
    }
}
