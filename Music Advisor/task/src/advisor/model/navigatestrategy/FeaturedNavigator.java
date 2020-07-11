package advisor.model.navigatestrategy;

import advisor.model.AdvisorHttpClient;
import advisor.model.Page;

import java.io.IOException;

public class FeaturedNavigator extends AbstractNavigator {

    public FeaturedNavigator(String next, String previous) {
        super(next, previous);
    }

    @Override
    public Page<?> next() throws IOException, InterruptedException {
        return ADVISOR_HTTP_CLIENT.getFeatured(next);
    }

    @Override
    public Page<?> prev() throws IOException, InterruptedException {
        return ADVISOR_HTTP_CLIENT.getFeatured(previous);
    }

}
